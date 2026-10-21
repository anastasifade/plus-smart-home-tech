package ru.yandex.practicum.order.exception;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {

    private final Throwable cause;

    public ServiceUnavailableException(String message) {
        this(message, null);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }
}
