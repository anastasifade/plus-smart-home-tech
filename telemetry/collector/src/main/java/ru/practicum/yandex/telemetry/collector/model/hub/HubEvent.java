package ru.practicum.yandex.telemetry.collector.model.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceAddedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceRemovedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = UnknownHubEvent.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
@Getter
@Setter
@ToString
public abstract class HubEvent {
    /**
     * Unique hub identifier.
     */
    @NotBlank(message = "Hub id cannot be null or blank.")
    private String hubId;

    /**
     * Event timestamp. Default - current timestamp.
     */
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
