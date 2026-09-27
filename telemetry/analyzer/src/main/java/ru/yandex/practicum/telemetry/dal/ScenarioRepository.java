package ru.yandex.practicum.telemetry.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.model.Scenario;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByHubId(String hubId);
    boolean existsByHubIdAndName(String hubId, String name);
    void deleteByHubIdAndName(String hubId, String name);
}
