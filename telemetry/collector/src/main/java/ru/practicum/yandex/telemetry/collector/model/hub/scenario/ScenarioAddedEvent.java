package ru.practicum.yandex.telemetry.collector.model.hub.scenario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.yandex.telemetry.collector.constants.ValidationConstants;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;

import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    /**
     * Unique scenario name, minimum character length: 3.
     */
    @NotBlank(message = "Scenario name cannot be null or blank.")
    @Length(min = ValidationConstants.MIN_SCENARIO_NAME_LENGTH,
            message = "Scenario name must be at least 3 characters long.")
    private String name;

    /**
     * Conditions that trigger the scenario.
     */
    @NotEmpty(message = "At least one condition must be added to the scenario.")
    private Set<@Valid ScenarioCondition> conditions;

    /**
     * Actions performed by devices as part of the scenario.
     */
    @NotEmpty(message = "At least one action must be added to the scenario.")
    private Set<@Valid DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
