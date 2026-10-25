package ru.yandex.practicum.common;

public record ReserveResponse(

        boolean success,

        Integer availableQuantity,

        String message
) {
}
