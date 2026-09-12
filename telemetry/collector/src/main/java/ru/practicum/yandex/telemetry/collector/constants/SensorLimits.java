package ru.practicum.yandex.telemetry.collector.constants;

public final class SensorLimits {
    private SensorLimits() {
    }

    public static final int MIN_CLIMATE_SENSOR_TEMP_C = -40;
    public static final int MAX_CLIMATE_SENSOR_TEMP_C = 85;
    public static final int MAX_CLIMATE_SENSOR_HUMIDITY = 100;
    public static final int MAX_CLIMATE_SENSOR_CO2_LEVEL = 40000;
    public static final int MAX_LQI = 255;
    public static final int MAX_LUMINOSITY = 100000;
    public static final int MIN_TEMPERATURE_SENSOR_TEMP_C = -60;
    public static final int MIN_TEMPERATURE_SENSOR_TEMP_F = -76;
    public static final int MAX_TEMPERATURE_SENSOR_TEMP_C = 300;
    public static final int MAX_TEMPERATURE_SENSOR_TEMP_F = 572;
}
