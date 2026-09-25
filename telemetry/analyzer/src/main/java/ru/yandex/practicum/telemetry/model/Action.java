package ru.yandex.practicum.telemetry.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

import java.util.Objects;

@Entity
@Table(name = "actions")
@Getter
@Setter
@ToString
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActionTypeAvro type;

    private Integer value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action action)) return false;
        if (this.id == null || action.id == null) return false;
        return Objects.equals(id, action.id) && type == action.type && Objects.equals(value, action.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
