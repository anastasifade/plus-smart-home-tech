package ru.yandex.practicum.telemetry.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
