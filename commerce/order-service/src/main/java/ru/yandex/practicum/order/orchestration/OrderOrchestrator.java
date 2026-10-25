package ru.yandex.practicum.order.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.order.dto.*;
import ru.yandex.practicum.order.exception.ReservationFailureException;
import ru.yandex.practicum.order.mapper.OrderItemMapper;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.mapper.ReserveRequestMapper;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrchestrator {

    private final ProductGateway productGateway;
    private final InventoryGateway inventoryGateway;
    private final OrderService service;

    public OrderDto createOrder(CreateOrderShortRequest createRequest) {
        log.debug("Order orchestrator: request to create a new order.");

        boolean degraded = false;

        Map<Long, Integer> quantitiesByProductId = getQuantitiesByProductId(createRequest.items());

        ProductGatewayResponse productResponse = productGateway.getValidProductsById(quantitiesByProductId.keySet());
        Map<Long, ProductDto> productsById = productResponse.products();
        if (productResponse.isDegraded()) {
            degraded = true;
        }

        Set<ReserveRequest> reservationRequests = getReserveRequests(quantitiesByProductId);

        InventoryGatewayResponse inventoryResponse = inventoryGateway.reserveItems(reservationRequests);
        if (inventoryResponse.isDegraded()) {
            degraded = true;
        }

        try {
            CreateOrderRequest fullRequest = getFullRequest(createRequest, quantitiesByProductId,
                    productsById, degraded);
            return service.createOrder(fullRequest);
        } catch (Exception e) {
            log.warn("Failed to create order. Releasing reservations.");
            if (!inventoryResponse.isDegraded()) {
                try {
                    inventoryGateway.releaseReservations(reservationRequests);
                } catch (ReservationFailureException exception) {
                    log.warn("Failed to release reservation for requests: {}.", reservationRequests);
                    e.addSuppressed(exception);
                }
            }
            throw e;
        }
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
                                              Map<Long, ProductDto> productsById,
                                              boolean isDegraded) {
        List<OrderItemRequest> itemRequests = productsById.keySet()
                .stream()
                .map(id -> OrderItemMapper.toFullRequest(productsById.get(id), quantitiesByProductId.get(id)))
                .toList();

        return OrderMapper.toFullRequest(shortRequest, itemRequests, isDegraded);
    }


}
