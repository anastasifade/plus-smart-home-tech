package ru.yandex.practicum.telemetry.service.snapshot;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.mapper.ActionRequestMapper;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.Scenario;
import ru.yandex.practicum.telemetry.service.hub.ScenarioService;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SnapshotServiceImpl implements SnapshotService {

    private final Map<ConditionOperationAvro, BiPredicate<Integer, Integer>> operations = Map.of(
            ConditionOperationAvro.GREATER_THAN, (stateValue, conditionValue) ->
                    stateValue > conditionValue,
            ConditionOperationAvro.EQUALS, Integer::equals,
            ConditionOperationAvro.LOWER_THAN, (stateValue, conditionValue) ->
                    stateValue < conditionValue
    );

    private final HubRouterControllerBlockingStub hubRouterClient;
    private final ScenarioService scenarioService;

    public SnapshotServiceImpl(@GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient,
                               ScenarioService scenarioService) {
        this.hubRouterClient = hubRouterClient;
        this.scenarioService = scenarioService;
    }

    @Override
    public void handle(String hubId, SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioService.getScenariosForHub(hubId);
        List<DeviceActionRequest> actions = scenarios.stream()
                .filter(scenario -> isValidScenario(scenario, snapshot))
                .peek(scenario -> log.info("Activating scenario: {}.", scenario.getName()))
                .map(scenario -> scenario.getActions().entrySet()
                        .stream()
                        .map(entry -> ActionRequestMapper.toDeviceActionRequest(hubId,
                                entry.getKey(), scenario.getName(), entry.getValue()))
                        .toList())
                .flatMap(List::stream)
                .toList();
        if (actions.isEmpty()) {
            log.info("No actions scheduled to be performed.");
        } else {
            log.debug("Actions triggered: {}.", actions);
        }
        actions.forEach(hubRouterClient::handleDeviceAction);
    }

    private boolean isValidScenario(Scenario scenario, SensorsSnapshotAvro snapshot) {
        Map<String, Condition> conditions = scenario.getConditions();
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();

        if (!sensorsState.keySet().containsAll(conditions.keySet())) {
            log.warn("Incomplete snapshot: some sensors from the scenario are not provided. " +
                    "No actions will be triggered.");
            log.trace("Sensors expected: {}. Sensors provided: {}.", conditions.keySet(), sensorsState.keySet());
            return false;
        }

        Map<Condition, SensorStateAvro> conditionsBySensor = conditions.keySet()
                .stream()
                .collect(Collectors.toMap(conditions::get, sensorsState::get));

        return conditionsBySensor.entrySet()
                .stream()
                .allMatch(entry -> isMet(entry.getKey(), entry.getValue()));
    }

    private boolean isMet(Condition condition, SensorStateAvro state) {
        Map<ConditionTypeAvro, Integer> valuesForConditionType = getValue(state);
        Integer value = valuesForConditionType.get(condition.getType());
        if (value == null) {
            log.error("No matching value found for condition type: {}. Condition marked as false.", condition.getType());
            log.trace("No matching value found. Condition: {}. Sensor State: {}.", condition, state);
            return false;
        }

        return operations.getOrDefault(condition.getOperation(), (a, b) -> {
            log.error("Unknown operation: {}.", condition.getOperation());
            return false;
        }).test(value, condition.getValue());
    }

    private Map<ConditionTypeAvro, Integer> getValue(SensorStateAvro state) {
        return switch (state.getData()) {
            case TemperatureSensorAvro temp -> Map.of(ConditionTypeAvro.TEMPERATURE, temp.getTemperatureC());
            case ClimateSensorAvro climate -> Map.of(ConditionTypeAvro.TEMPERATURE, climate.getTemperatureC(),
                    ConditionTypeAvro.HUMIDITY, climate.getHumidity(),
                    ConditionTypeAvro.CO2LEVEL, climate.getCo2Level());
            case MotionSensorAvro motion -> Map.of(ConditionTypeAvro.MOTION, motion.getMotion() ? 1 : 0);
            case LightSensorAvro light -> Map.of(ConditionTypeAvro.LUMINOSITY, light.getLuminosity());
            case SwitchSensorAvro switchSensor -> Map.of(ConditionTypeAvro.SWITCH, switchSensor.getState() ? 1 : 0);
            default -> Map.of();
        };
    }


}
