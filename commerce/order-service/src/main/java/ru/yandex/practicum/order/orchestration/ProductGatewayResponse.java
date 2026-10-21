package ru.yandex.practicum.order.orchestration;

import ru.yandex.practicum.common.ProductDto;

import java.util.Map;

public record ProductGatewayResponse(
        Map<Long, ProductDto> products,
        boolean isDegraded
) {
}
