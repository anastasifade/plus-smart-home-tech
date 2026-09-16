package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public final class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    @Override
    protected ScenarioRemovedEventAvro toAvro(HubEventProto event) {
        return HubEventMapper.toAvro(event.getScenarioRemoved());
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }
}
