package ru.yandex.practicum.order.exception;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message);
    }
}