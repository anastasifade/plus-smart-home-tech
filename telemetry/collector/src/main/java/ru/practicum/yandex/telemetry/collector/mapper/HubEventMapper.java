package ru.practicum.yandex.telemetry.collector.mapper;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceAddedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceRemovedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.DeviceAction;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioCondition;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

@UtilityClass
public class HubEventMapper {
    public <T extends SpecificRecordBase> HubEventAvro toAvro(HubEvent event, T payload) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    public DeviceAddedEventAvro toAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().toString()))
                .build();
    }

    public DeviceRemovedEventAvro toAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    public ScenarioAddedEventAvro toAvro(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(event.getActions()
                        .stream()
                        .map(HubEventMapper::toAvro)
                        .toList())
                .setConditions(event.getConditions()
                        .stream()
                        .map(HubEventMapper::toAvro)
                        .toList())
                .build();
    }

    public DeviceActionAvro toAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                .setValue(action.getValue())
                .build();
    }

    public ScenarioConditionAvro toAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setValue(condition.getValue())
                .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .build();
    }

    public ScenarioRemovedEventAvro toAvro(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}
