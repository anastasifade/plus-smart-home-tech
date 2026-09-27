package ru.yandex.practicum.telemetry.service.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotService {
    void handle(String hubId, SensorsSnapshotAvro snapshot);
}
