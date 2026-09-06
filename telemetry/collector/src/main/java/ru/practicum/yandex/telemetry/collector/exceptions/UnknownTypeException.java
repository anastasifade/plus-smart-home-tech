package ru.practicum.yandex.telemetry.collector.exceptions;

public class UnknownTypeException extends RuntimeException {
    public UnknownTypeException(String message) {
        super(message);
    }
}
