package ru.yandex.practicum.telemetry.service.hub;

import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

public interface DeviceService {
    void add(String hubId, DeviceAddedEventAvro device);
    void remove(String hubId, DeviceRemovedEventAvro device);
}
