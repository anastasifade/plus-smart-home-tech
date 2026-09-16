package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Component
public final class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    @Override
    protected ScenarioAddedEventAvro toAvro(HubEventProto event) {
        return HubEventMapper.toAvro(event.getScenarioAdded());
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}
