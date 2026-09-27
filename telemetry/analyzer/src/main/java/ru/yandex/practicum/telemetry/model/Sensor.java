package ru.yandex.practicum.telemetry.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "sensors")
@Getter
@Setter
public class Sensor {
    @Id
    private String id;

    @Column(nullable = false)
    private String hubId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return Objects.equals(id, sensor.id) && Objects.equals(hubId, sensor.hubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hubId);
    }
}
