package ru.practicum.yandex.telemetry.collector.model.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScenarioCondition {
    /**
     * Identifier of the device that the condition is applied to.
     */
    @NotBlank(message = "Device (sensor) id cannot be null or blank.")
    private String sensorId;

    /**
     * Type of the condition applied to the device.
     */
    @NotNull(message = "Scenario condition type cannot be null.")
    private ScenarioConditionType type;

    /**
     * Operation used in scenario condition.
     */
    @NotNull(message = "Scenario condition operation cannot be null.")
    private ScenarioConditionOperation operation;

    /**
     * Value, against which sensor data is evaluated in the given condition.
     */
    @NotNull(message = "Scenario condition value cannot be null.")
    private Integer value;
}
