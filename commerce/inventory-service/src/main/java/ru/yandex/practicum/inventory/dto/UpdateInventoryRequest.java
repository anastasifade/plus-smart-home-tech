package ru.yandex.practicum.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateInventoryRequest(

        @NotNull(message = "Product ID is required.")
        Long productId,

        @NotNull(message = "Product quantity is required.")
        @Min(value = 0, message = "Product quantity cannot be negative.")
        Integer quantity
) {
}
