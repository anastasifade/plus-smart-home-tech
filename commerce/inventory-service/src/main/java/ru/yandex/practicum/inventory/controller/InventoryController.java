package ru.yandex.practicum.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.service.InventoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @Loggable
    @GetMapping
    public List<InventoryDto> findAll() {
        log.info("New request: GET /inventory.");
        return service.findAll();
    }

    @Loggable
    @GetMapping("/{productId}")
    public InventoryDto findByProductId(@PathVariable Long productId) {
        log.info("New request: GET /inventory/{}.", productId);
        return service.findByProductId(productId);
    }

    @Loggable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto createItem(@Valid @RequestBody UpdateInventoryRequest request) {
        log.info("New request: POST /inventory. Creating inventory item for product [id = {}], quantity = {}.",
                request.productId(), request.quantity());
        return service.createInventoryItem(request);
    }

    @Loggable
    @PutMapping
    public InventoryDto updateItem(@Valid @RequestBody UpdateInventoryRequest request) {
        log.info("New request: PUT /inventory. Updating product [id = {}], new quantity = {}.",
                request.productId(), request.quantity());
        log.debug("Request to update inventory item: {}.", request);
        return service.updateInventoryItem(request);
    }

    @Loggable
    @PostMapping("/reserve")
    public ReserveResponse reserve(@Valid @RequestBody ReserveRequest request) {
        log.info("New request: POST /inventory/reserve. Request to reserve product [id = {}], quantity = {}.",
                request.productId(), request.quantity());
        return service.reserveItem(request);
    }
}
