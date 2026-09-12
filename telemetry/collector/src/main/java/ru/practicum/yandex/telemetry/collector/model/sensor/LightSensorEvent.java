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
public class LightSensorEvent extends SensorEvent {

    /**
     * Link Quality Indicator (LQI) value, range 0 - 255.
     */
    @PositiveOrZero(message = "Link quality value cannot be a negative number.")
    @Max(value = SensorLimits.MAX_LQI, message = "Link quality value cannot exceed 255.")
    @NotNull(message = "Link quality value cannot be null.")
    private Integer linkQuality;

    /**
     * Luminosity value, supported range 0 - 100 000 lux.
     */
    @PositiveOrZero(message = "Luminosity cannot be a negative number.")
    @Max(value = SensorLimits.MAX_LUMINOSITY, message = "Luminosity value cannot exceed 100 000 lux.")
    @NotNull(message = "Luminosity value cannot be null.")
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
