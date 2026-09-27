package ru.yandex.practicum.telemetry.service.hub;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.model.Scenario;

import java.util.List;

public interface ScenarioService {
    void add(String hubId, ScenarioAddedEventAvro scenario);
    void remove(String hubId, ScenarioRemovedEventAvro scenario);
    List<Scenario> getScenariosForHub(String hubId);
}
