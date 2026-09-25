package ru.yandex.practicum.telemetry.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.dal.ActionRepository;
import ru.yandex.practicum.telemetry.dal.ConditionRepository;
import ru.yandex.practicum.telemetry.dal.ScenarioRepository;
import ru.yandex.practicum.telemetry.mapper.ScenarioMapper;
import ru.yandex.practicum.telemetry.model.Action;
import ru.yandex.practicum.telemetry.model.Condition;
import ru.yandex.practicum.telemetry.model.Scenario;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScenarioServiceImpl implements ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public void add(String hubId, ScenarioAddedEventAvro msg) {
        if (scenarioRepository.existsByHubIdAndName(hubId, msg.getName())) {
            log.warn("Scenario [name = {}] already exists in hub [id = {}]. No new scenario added.",
                    msg.getName(), hubId);
            return;
        }

        Scenario scenario = ScenarioMapper.toScenario(hubId, msg);

        scenario.getActions().values().forEach(this::saveAction);
        scenario.getConditions().values().forEach(this::saveCondition);

        Long id = scenarioRepository.save(scenario).getId();
        log.info("Scenario [id = {}] successfully created in hub [id = {}].", id, hubId);
    }

    @Override
    public void remove(String hubId, ScenarioRemovedEventAvro msg) {
        if (!scenarioRepository.existsByHubIdAndName(hubId, msg.getName())) {
            log.debug("Scenario [name = {}] does not exist in hub [id = {}]. No scenarios deleted.",
                    msg.getName(), hubId);
            return;
        }

        scenarioRepository.deleteByHubIdAndName(hubId, msg.getName());
        log.info("Scenario [name = {}] successfully removed from hub [id = {}].", msg.getName(), hubId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosForHub(String hubId) {
        log.trace("Requesting all scenarios for hub [id = {}].", hubId);
        return scenarioRepository.findByHubId(hubId);
    }

    private void saveAction(Action action) {
        actionRepository.save(action);
        log.debug("Action [details = {}] created successfully.", action);
    }

    private void saveCondition(Condition condition) {
        conditionRepository.save(condition);
        log.debug("Condition [details = {}] created successfully.", condition);
    }
}
