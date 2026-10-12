package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Loggable
    @GetMapping
    public List<CategoryDto> findAll() {
        log.info("New request: GET /categories.");
        return service.findAll();
    }

    @Loggable
    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        log.info("New request: GET /categories/{}.", id);
        return service.findById(id);
    }

    @Loggable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("New request: POST /categories. Creating category [name={}].", request.name());
        log.debug("Request to create category: {}.", request);
        return service.create(request);
    }
}
