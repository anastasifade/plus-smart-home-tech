package ru.practicum.yandex.telemetry.collector.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.yandex.telemetry.collector.constants.SensorLimits;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {

    /**
     * Temperature value (in Celsius).
     * Supports temperatures in range from -40C to +85C.
     */
    @Min(value = SensorLimits.MIN_CLIMATE_SENSOR_TEMP_C, message = "Temperature value cannot be below -40C.")
    @Max(value = SensorLimits.MAX_CLIMATE_SENSOR_TEMP_C, message = "Temperature value cannot be above 85C.")
    @NotNull(message = "Temperature value cannot be null.")
    private Integer temperatureC;

    /**
     * Relative humidity value, supported range 0-100%.
     */
    @PositiveOrZero(message = "Humidity value cannot be below 0%.")
    @Max(value = SensorLimits.MAX_CLIMATE_SENSOR_HUMIDITY, message = "Humidity level cannot be above 100%.")
    @NotNull(message = "Humidity value cannot be null.")
    private Integer humidity;

    /**
     * Carbon dioxide (CO2) level value (in ppm). Supported range 0 - 40 000 ppm.
     */
    @PositiveOrZero(message = "CO2 level cannot be below 0 ppm")
    @Max(value = SensorLimits.MAX_CLIMATE_SENSOR_CO2_LEVEL, message = "CO2 level cannot be above 40 000 ppm.")
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
