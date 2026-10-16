package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;

@Slf4j
@Loggable
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    public List<OrderDto> findAll() {
        log.info("New request: GET /orders.");
        return service.findAll();
    }

    @GetMapping("/by-email")
    public List<OrderDto> findByEmail(@RequestParam @Email String email) {
        log.info("New request: GET /orders/by-email?{}.", email);
        return service.findByClient(email);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        log.info("New request: GET /orders/{}.", id);
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody CreateOrderRequest request) {
        log.info("New request: POST /orders.");
        log.debug("Request to create order: {}.", request);
        return service.createOrder(request);
    }
}
