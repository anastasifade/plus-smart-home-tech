package ru.yandex.practicum.inventory.exception;

public class ReservationReleaseException extends RuntimeException {
    public ReservationReleaseException(String message) {
        super(message);
    }
}
