package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.order.dto.CreateOrderShortRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.orchestration.OrderOrchestrator;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;

@Slf4j
@Loggable
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final OrderOrchestrator orchestrator;

    @GetMapping
    public List<OrderDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/by-email")
    public List<OrderDto> findByEmail(@RequestParam @Email String email) {
        return service.findByClient(email);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody CreateOrderShortRequest request) {
        return orchestrator.createOrder(request);
    }
}
