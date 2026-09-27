package ru.yandex.practicum.telemetry.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdAndHubId(String id, String hubId);
}
