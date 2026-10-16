package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemShortRequest(
        @NotNull(message = "Product ID is required.")
        Long productId,

        @NotNull(message = "Item quantity is required.")
        @Min(value = 1, message = "Item quantity cannot be less than 1.")
        Integer quantity
) {
}
