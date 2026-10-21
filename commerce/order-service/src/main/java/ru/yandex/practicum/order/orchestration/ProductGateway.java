package ru.yandex.practicum.order.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.order.client.ProductClient;
import ru.yandex.practicum.order.exception.BadRequestException;
import ru.yandex.practicum.order.exception.ConflictException;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.exception.ServiceUnavailableException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductGateway {

    private final ProductClient client;

    public ProductGatewayResponse getValidProductsById(Set<Long> ids) {
        ServiceCallResult<Set<ProductDto>> productServiceCallResult = getProductsInfo(ids);

        if (productServiceCallResult instanceof ServiceCallResult.Failure<Set<ProductDto>>(String message)) {
            throw new OrderProcessingException(message);
        }

        if (productServiceCallResult instanceof ServiceCallResult.Success<Set<ProductDto>>(Set<ProductDto> products)) {
            validateAllProductsFound(ids, products);
            validateAllProductsActive(products);

            log.debug("All products found and validated.");
            return new ProductGatewayResponse(toMap(products), false);
        }

        if (productServiceCallResult instanceof ServiceCallResult.Degraded<Set<ProductDto>> degraded) {
            Set<ProductDto> degradedProducts = ids.stream()
                    .map(id -> new ProductDto(id, "[PLACEHOLDER]", null, BigDecimal.valueOf(-1),
                            null, null, null))
                    .collect(Collectors.toSet());
            log.debug("Product service unavailable, returning product placeholders.");
            return new ProductGatewayResponse(toMap(degradedProducts), true);
        }

        log.error("Unknown ServiceCallResult type: {}.", productServiceCallResult);
        throw new IllegalArgumentException("Unknown ServiceCallResult type.");
    }

    private ServiceCallResult<Set<ProductDto>> getProductsInfo(Set<Long> ids) {
        try {
            return new ServiceCallResult.Success<>(client.getProductsByIds(ids));
        } catch (BadRequestException | ConflictException e) {
            return new ServiceCallResult.Failure<>("Could not process order.");
        } catch (ServiceUnavailableException e) {
            return new ServiceCallResult.Degraded<>(e.getMessage());
        }
    }

    private Map<Long, ProductDto> toMap(Set<ProductDto> products) {
        return products
                .stream()
                .collect(Collectors.toMap(ProductDto::id, Function.identity()));
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
}
