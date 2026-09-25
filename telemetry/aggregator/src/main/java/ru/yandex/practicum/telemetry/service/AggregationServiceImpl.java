package ru.yandex.practicum.telemetry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.mapper.SnapshotMapper;
import ru.yandex.practicum.telemetry.dal.SnapshotStorage;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public final class AggregationServiceImpl implements AggregationService {

    private final SnapshotStorage storage;

    @Override
    public Optional<SensorsSnapshotAvro> handle(SensorEventAvro event) {
        log.debug("Aggregation Service: new sensor event received: {}.", event);
        Optional<SensorsSnapshotAvro> snapshotOpt = storage.get(event.getHubId());
        return snapshotOpt.map(snapshot -> updateSnapshot(snapshot, event))
                .orElseGet(() -> newSnapshot(event));
    }

    private Optional<SensorsSnapshotAvro> newSnapshot(SensorEventAvro event) {
        log.info("Creating new snapshot. HubId = {}, timestamp = {}.", event.getHubId(), event.getTimestamp());
        SensorsSnapshotAvro snapshot = SnapshotMapper.toSnapshot(event);
        storage.save(snapshot);
        return Optional.of(snapshot);
    }

    private Optional<SensorsSnapshotAvro> updateSnapshot(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        if (snapshot.getSensorsState().get(event.getId()) == null) {
            log.info("Updating snapshot [hubId={}]: adding new event state.", event.getHubId());
            return updateAndSave(snapshot, event);
        }

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (event.getTimestamp().isBefore(oldState.getTimestamp())) {
            log.info("Sensor event [timestamp={}] outdated. Snapshot [hubId={}, sensor state timestamp={}] not updated.",
                    event.getTimestamp(), event.getHubId(), oldState.getTimestamp());
            return Optional.empty();
        }

        if (oldState.getData().equals(event.getPayload())) {
            log.info("No changes detected. Snapshot [hubId={}] not updated,", event.getHubId());
            return Optional.empty();
        }

        log.info("Updating sensors state snapshot: hubId={}, timestamp={}.", event.getHubId(), event.getTimestamp());
        log.trace("Details: old state = {}; new state = {}.", oldState.getData(), event.getPayload());
        return updateAndSave(snapshot, event);
    }

    private Optional<SensorsSnapshotAvro> updateAndSave(SensorsSnapshotAvro snapshot, SensorEventAvro event) {
        snapshot = SnapshotMapper.updateSnapshot(snapshot, event);
        log.trace("Snapshot mapped successfully.");
        storage.save(snapshot);
        log.debug("Snapshot saved to storage.");
        return Optional.of(snapshot);
    }
}
