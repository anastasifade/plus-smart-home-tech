package ru.practicum.yandex.telemetry.collector.model.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString
public class ErrorResponse {
    private String message;
    private List<Object> errors;
    private Instant timestamp;
}
