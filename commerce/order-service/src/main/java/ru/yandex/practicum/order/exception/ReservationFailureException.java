package ru.yandex.practicum.order.exception;

public class ReservationFailureException extends RuntimeException {
    public ReservationFailureException(String message) {
        super(message);
    }
}
