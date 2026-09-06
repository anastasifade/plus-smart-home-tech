package ru.practicum.yandex.telemetry.collector.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.model.error.ErrorResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(Arrays.asList(e.getStackTrace()), message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(UnknownTypeException e) {
        return buildResponse(Arrays.asList(e.getStackTrace()), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Throwable e) {
        String msg = e.getMessage();
        List<Object> errors = Arrays.asList(e.getStackTrace());

        log.error("Unexpected error. Message: {}.", msg);
        log.debug("Error details: {}.", errors);

        return buildResponse(errors, msg);
    }

    private ErrorResponse buildResponse(List<Object> errors, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setErrors(errors);
        response.setMessage(message);
        response.setTimestamp(Instant.now());
        return response;
    }
}
