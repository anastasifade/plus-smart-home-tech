package ru.practicum.yandex.telemetry.collector.model.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceAction {
    /**
     * Identifier of the device that the action is applied to.
     */
    @NotBlank(message = "Device (sensor) id cannot be null or blank.")
    private String sensorId;

    /**
     * Type of action to be performed on the device.
     */
    @NotNull(message = "Device action type cannot be null.")
    private DeviceActionType type;

    /**
     * (Optional) Device action value, used with DeviceActionType=SET_VALUE.
     */
    private Integer value;
}
