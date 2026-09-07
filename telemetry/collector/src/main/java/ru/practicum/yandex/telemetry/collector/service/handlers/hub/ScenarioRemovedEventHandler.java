package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public final class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    @Override
    protected ScenarioRemovedEventAvro toAvro(HubEvent event) {
        return HubEventMapper.toAvro((ScenarioRemovedEvent) event);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
