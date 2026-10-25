package ru.yandex.practicum.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReserveRequest(

        @NotNull(message = "Product ID is required.")
        Long productId,

        @NotNull(message = "Product quantity is required.")
        @Min(value = 1, message = "Product quantity cannot be less than 1.")
        Integer quantity
) {
}
