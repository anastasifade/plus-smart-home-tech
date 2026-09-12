package ru.practicum.yandex.telemetry.collector.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.yandex.telemetry.collector.constants.SensorLimits;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    /**
     * Link Quality Indicator (LQI) value, range 0 - 255.
     */
    @PositiveOrZero(message = "Link quality value cannot be a negative number.")
    @Max(value = SensorLimits.MAX_LQI, message = "Link quality value cannot exceed 255.")
    @NotNull(message = "Link quality value cannot be null.")
    private Integer linkQuality;

    /**
     * Represents whether the motion was detected (true) or not (false).
     */
    @NotNull(message = "Motion value cannot be null.")
    private Boolean motion;

    @PositiveOrZero(message = "Voltage value cannot be a negative number.")
    @NotNull(message = "Voltage value cannot be null.")
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
