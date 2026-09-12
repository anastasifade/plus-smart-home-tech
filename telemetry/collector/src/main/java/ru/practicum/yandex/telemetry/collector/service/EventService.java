package ru.practicum.yandex.telemetry.collector.service;

import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;

public interface EventService {
    void postSensorEvent(SensorEvent event);
    void postHubEvent(HubEvent event);
}
