package ru.yandex.practicum.telemetry.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

import java.util.Objects;

@Entity
@Table(name = "conditions")
@Getter
@Setter
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ConditionTypeAvro type;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ConditionOperationAvro operation;

    private Integer value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition condition)) return false;
        if (this.id == null || condition.id == null) return false;
        return Objects.equals(id, condition.id) && type == condition.type && operation == condition.operation && Objects.equals(value, condition.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, operation, value);
    }
}
