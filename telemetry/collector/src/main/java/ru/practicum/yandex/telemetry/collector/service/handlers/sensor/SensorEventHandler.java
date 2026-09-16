package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventProto.PayloadCase getType();

    void handle(SensorEventProto event);
}
