package ru.yandex.practicum.order.orchestration;

public sealed interface ServiceCallResult<T> {

    record Success<T>(T value) implements ServiceCallResult<T> { }

    record Failure<T>(String message) implements ServiceCallResult<T> { }

    record Degraded<T>(String reason) implements ServiceCallResult<T> { }
}
