package ru.yandex.practicum.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "Customer name is required.")
        String customerName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email.")
        String customerEmail,

        @NotEmpty(message = "An order must contain at least one item.")
        @Valid
        List<OrderItemRequest> items
) {
}