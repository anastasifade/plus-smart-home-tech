package ru.practicum.yandex.telemetry.collector.model.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.yandex.telemetry.collector.constants.ValidationConstants;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    /**
     * Unique scenario name.
     */
    @NotBlank(message = "Scenario name cannot be null or blank.")
    @Length(min = ValidationConstants.MIN_SCENARIO_NAME_LENGTH,
            message = "Scenario name must be at least 3 characters long.")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
