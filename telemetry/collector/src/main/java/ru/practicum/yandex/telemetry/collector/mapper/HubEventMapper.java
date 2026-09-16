package ru.practicum.yandex.telemetry.collector.mapper;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@UtilityClass
public class HubEventMapper {
    public <T extends SpecificRecordBase> HubEventAvro toAvro(HubEventProto event, T payload) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

    public DeviceAddedEventAvro toAvro(DeviceAddedEventProto event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getType().toString()))
                .build();
    }

    public DeviceRemovedEventAvro toAvro(DeviceRemovedEventProto event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    public ScenarioAddedEventAvro toAvro(ScenarioAddedEventProto event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(event.getActionList()
                        .stream()
                        .map(HubEventMapper::toAvro)
                        .toList())
                .setConditions(event.getConditionList()
                        .stream()
                        .map(HubEventMapper::toAvro)
                        .toList())
                .build();
    }

    public DeviceActionAvro toAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                .setValue(action.getValue())
                .build();
    }

    public ScenarioConditionAvro toAvro(ScenarioConditionProto condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setValue(condition.hasBoolValue() ? condition.getBoolValue() : condition.getIntValue())
                .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .build();
    }

    public ScenarioRemovedEventAvro toAvro(ScenarioRemovedEventProto event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}
