package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.service.ProductService;

import java.util.List;
import java.util.Set;

@Slf4j
@Loggable
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public Set<ProductDto> findAll(@RequestParam(value = "ids", required = false) Set<Long> ids) {
        return ids == null ? service.findAll() : service.findAllByIds(ids);
    }

    @GetMapping("/category/{categoryId}")
    public Set<ProductDto> findByCategory(@PathVariable Long categoryId) {
        return service.findAllByCategory(categoryId);
    }

    @GetMapping("/search")
    public Set<ProductDto> search(@RequestParam String query) {
        return service.search(query);
    }

    @Loggable
    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody CreateProductRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable Long id,
                             @Valid @RequestBody UpdateProductRequest request) {
        return service.update(id, request);
    }
}
