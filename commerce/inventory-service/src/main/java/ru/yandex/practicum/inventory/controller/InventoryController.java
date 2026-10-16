package ru.yandex.practicum.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.service.InventoryService;

import java.util.List;
import java.util.Set;

@Slf4j
@Loggable
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    public List<InventoryDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{productId}")
    public InventoryDto findByProductId(@PathVariable Long productId) {
        return service.findByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto createItem(@Valid @RequestBody UpdateInventoryRequest request) {
        return service.createInventoryItem(request);
    }

    @PutMapping
    public InventoryDto updateItem(@Valid @RequestBody UpdateInventoryRequest request) {
        return service.updateInventoryItem(request);
    }

    @PutMapping("/reserve/batch")
    public Set<ReserveResponse> reserve(@Valid @RequestBody Set<ReserveRequest> requests) {
        return service.reserveItems(requests);
    }

    @PostMapping("/reserve")
    public ReserveResponse reserve(@Valid @RequestBody ReserveRequest request) {
        return service.reserveItem(request);
    }

    @PostMapping("/release/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void release(@Valid @RequestBody Set<ReserveRequest> requests) {
        service.releaseItems(requests);
    }

    @PostMapping("/release")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void release(@Valid @RequestBody ReserveRequest request) {
        service.releaseItem(request);
    }
}
