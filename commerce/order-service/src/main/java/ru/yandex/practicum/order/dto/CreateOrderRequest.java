package ru.yandex.practicum.order.dto;

import java.util.List;

public record CreateOrderRequest(
        String customerName,

        String customerEmail,

        List<OrderItemRequest> items,

        boolean isDegraded
) {
}