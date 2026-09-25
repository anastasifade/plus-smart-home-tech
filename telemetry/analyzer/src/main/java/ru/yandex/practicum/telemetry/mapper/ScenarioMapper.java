package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.model.Action;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.Scenario;

import java.util.stream.Collectors;

@UtilityClass
public class ScenarioMapper {

    public Scenario toScenario(String hubId, ScenarioAddedEventAvro msg) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(msg.getName());
        scenario.setActions(msg.getActions()
                .stream()
                .collect(Collectors.toMap(DeviceActionAvro::getSensorId, ScenarioMapper::toAction)));
        scenario.setConditions(msg.getConditions()
                .stream()
                .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, ScenarioMapper::toCondition)));
        return scenario;
    }

    public Action toAction(DeviceActionAvro msg) {
        Action action = new Action();
        action.setType(msg.getType());
        action.setValue(msg.getValue());
        return action;
    }

    public Condition toCondition(ScenarioConditionAvro msg) {
        Condition condition = new Condition();
        condition.setOperation(msg.getOperation());
        condition.setType(msg.getType());

        Object val = msg.getValue();
        Integer value;
        if (val instanceof Boolean) {
            value = (Boolean) val ? 1 : 0;
        } else {
            value = (Integer) val;
        }

        condition.setValue(value);
        return condition;
    }
}
