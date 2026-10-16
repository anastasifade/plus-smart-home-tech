package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequest(

        @NotNull(message = "Product ID is required.")
        Long productId,

        @NotBlank(message = "Product name is required.")
        String productName,

        @NotNull(message = "Item quantity is required.")
        @Min(value = 1, message = "Item quantity cannot be less than 1.")
        Integer quantity,

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.01", message = "Price must be above 0.00.")
        BigDecimal price
) {
}
