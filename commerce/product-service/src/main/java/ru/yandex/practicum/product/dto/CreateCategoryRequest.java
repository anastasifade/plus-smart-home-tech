package ru.yandex.practicum.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.common.validation.NullOrNotBlank;

public record CreateCategoryRequest(

        @NotBlank(message = "Category name is required.")
        @Size(max = 255, message = "Category name cannot exceed 255 symbols.")
        String name,

        @NullOrNotBlank(message = "Category description cannot be blank.")
        @Size(max = 500, message = "Category description cannot exceed 500 symbols.")
        String description
) {
}