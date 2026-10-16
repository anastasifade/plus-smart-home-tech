package ru.yandex.practicum.order.orchestration;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.order.client.InventoryClient;
import ru.yandex.practicum.order.client.ProductClient;
import ru.yandex.practicum.order.dto.*;
import ru.yandex.practicum.order.exception.*;
import ru.yandex.practicum.order.mapper.OrderItemMapper;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.mapper.ReserveRequestMapper;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrchestrator {

    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final OrderService service;

    public OrderDto createOrder(CreateOrderShortRequest createRequest) {
        log.debug("Order orchestrator: request to create a new order.");

        Map<Long, Integer> quantitiesByProductId = getQuantitiesByProductId(createRequest.items());
        Map<Long, ProductDto> productsById = getValidProductsById(quantitiesByProductId.keySet());
        Set<ReserveRequest> reservationRequests = getReserveRequests(quantitiesByProductId);

        reserveItems(reservationRequests);

        try {
            CreateOrderRequest fullRequest = getFullRequest(createRequest, quantitiesByProductId, productsById);
            return service.createOrder(fullRequest);
        } catch (Exception e) {
            log.warn("Failed to create order. Releasing reservations.");
            try {
                releaseReservations(reservationRequests);
            } catch (ReservationFailureException exception) {
                log.warn("Failed to release reservation for requests: {}.", reservationRequests);
                e.addSuppressed(exception);
            }
            throw e;
        }
    }

    private Map<Long, ProductDto> getValidProductsById(Set<Long> ids) {
        Set<ProductDto> products;

        try {
            products = productClient.getProductsById(ids);
        } catch (BadRequestException | ConflictException e) {
            throw new OrderProcessingException("Could not process order.");
        }

        validateAllProductsFound(ids, products);
        validateAllProductsActive(products);

        log.debug("All products found and validated.");
        return products
                .stream()
                .collect(Collectors.toMap(ProductDto::id, Function.identity()));
    }

    private Map<Long, Integer> getQuantitiesByProductId(List<OrderItemShortRequest> requests) {
        return requests
                .stream()
                .collect(Collectors.toMap(OrderItemShortRequest::productId,
                        OrderItemShortRequest::quantity,
                        Integer::sum));
    }

    private Set<ReserveRequest> getReserveRequests(Map<Long, Integer> quantitiesByProductId) {
        return quantitiesByProductId.entrySet()
                .stream()
                .map(entry -> ReserveRequestMapper.toRequest(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    private CreateOrderRequest getFullRequest(CreateOrderShortRequest shortRequest,
                                                   Map<Long, Integer> quantitiesByProductId,
                                                   Map<Long, ProductDto> productsById) {
        List<OrderItemRequest> itemRequests = productsById.keySet()
                .stream()
                .map(id -> OrderItemMapper.toFullRequest(productsById.get(id), quantitiesByProductId.get(id)))
                .toList();

        return OrderMapper.toFullRequest(shortRequest, itemRequests);
    }

    private void validateAllProductsFound(Set<Long> ids, Set<ProductDto> products) {
       Set<Long> foundIds = products.stream()
                .map(ProductDto::id)
                .collect(Collectors.toSet());

       Set<Long> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

       if (!missingIds.isEmpty()) {
           throw new OrderProcessingException(String.format("Some or all products were not found [ids = %s].",
                   missingIds));
       }
    }

    private void validateAllProductsActive(Set<ProductDto> products) {
        Set<Long> inactiveIds = products.stream()
                .filter(product -> !product.active())
                .map(ProductDto::id)
                .collect(Collectors.toSet());

        if (!inactiveIds.isEmpty()) {
            throw new OrderProcessingException(String.format("Some or all products are no longer available [ids = %s].",
                    inactiveIds));
        }
    }

    private void reserveItems(Set<ReserveRequest> requests) {
        Set<ReserveResponse> responses;
        try {
            responses = inventoryClient.reserveStock(requests);
        } catch (NotFoundException e) {
            throw new OrderProcessingException("Some or all products not found in stock.");
        } catch (ConflictException e) {
            throw new OrderProcessingException("Insufficient stock.");
        }

        Set<ReserveResponse> failedRequests = responses.stream()
                .filter(response -> !response.success())
                .collect(Collectors.toSet());

        if (!failedRequests.isEmpty()) {
            releaseReservations(requests);
            throw new OrderProcessingException("Failed to reserve products.");
        }

        log.info("All items successfully reserved.");
    }

    private void releaseReservations(Set<ReserveRequest> requests) {
        try {
            inventoryClient.releaseItems(requests);
        } catch (BadRequestException e) {
            log.error("Failed to release reservation on items. Requests: {}. Error details: {}.",
                    requests, e.getMessage());
            throw new ReservationFailureException("Failed to release reservation on items.");
        }
    }
}
