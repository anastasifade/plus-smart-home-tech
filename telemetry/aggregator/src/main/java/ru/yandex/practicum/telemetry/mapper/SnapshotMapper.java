package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;

@UtilityClass
public class SnapshotMapper {
    public SensorsSnapshotAvro toSnapshot(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setSensorsState(new HashMap<>())
                .setTimestamp(event.getTimestamp())
                .build();

        snapshot.getSensorsState().put(event.getId(), toSensorState(event));
        return snapshot;
    }

    public SensorsSnapshotAvro updateSnapshot(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        snapshot.getSensorsState().put(event.getId(), toSensorState(event));
        snapshot.setTimestamp(event.getTimestamp());
        return snapshot;
    }

    public SensorStateAvro toSensorState(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }

}
