package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Component
public final class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    @Override
    protected ScenarioAddedEventAvro toAvro(HubEvent event) {
        return HubEventMapper.toAvro((ScenarioAddedEvent) event);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
