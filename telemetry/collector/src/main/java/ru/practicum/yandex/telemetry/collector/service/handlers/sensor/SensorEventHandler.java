package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getType();

    void handle(SensorEvent event);
}
