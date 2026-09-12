package ru.practicum.yandex.telemetry.collector.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.yandex.telemetry.collector.constants.SensorLimits;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {

    /**
     * Temperature (Celsius).
     */
    @Min(value = SensorLimits.MIN_TEMPERATURE_SENSOR_TEMP_C, message = "Temperature value cannot be below -60 C.")
    @Max(value = SensorLimits.MAX_TEMPERATURE_SENSOR_TEMP_C, message = "Temperature value cannot be above 300 C.")
    @NotNull(message = "Temperature, C value cannot be null.")
    private Integer temperatureC;

    /**
     * Temperature (Fahrenheit).
     */
    @Min(value = SensorLimits.MIN_TEMPERATURE_SENSOR_TEMP_F, message = "Temperature value cannot be below -76 F.")
    @Max(value = SensorLimits.MAX_TEMPERATURE_SENSOR_TEMP_F, message = "Temperature value cannot be above 572 F.")
    @NotNull(message = "Temperature, F value cannot be null.")
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
