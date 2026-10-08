package ru.yandex.practicum.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.common.validation.NullOrNotBlank;

import java.math.BigDecimal;

public record UpdateProductRequest(

        @NullOrNotBlank(message = "Product name cannot be blank.")
        @Size(max = 255, message = "Product name cannot exceed 255 characters.")
        String name,

        @NullOrNotBlank(message = "Product description cannot be blank.")
        @Size(max = 2000, message = "Product description cannot exceed 2000 characters.")
        String description,

        @DecimalMin(value = "0.01", message = "Product price must be above 0.00.")
        BigDecimal price,

        Long categoryId,

        @NullOrNotBlank(message = "Product image URL cannot be blank.")
        String imageUrl,

        Boolean active
) {
}