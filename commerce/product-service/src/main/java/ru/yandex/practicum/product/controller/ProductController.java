package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.logging.Loggable;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.service.ProductService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Loggable
    @GetMapping
    public List<ProductDto> findAll() {
        log.info("New request: GET /products.");
        return service.findAll();
    }

    @Loggable
    @GetMapping("/category/{categoryId}")
    public List<ProductDto> findByCategory(@PathVariable Long categoryId) {
        log.info("New request: GET /products/category/{}.", categoryId);
        return service.findAllByCategory(categoryId);
    }

    @Loggable
    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam String query) {
        log.info("New request: GET /products/search?{}.", query);
        return service.search(query);
    }

    @Loggable
    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        log.info("New request: GET /products/{}.", id);
        return service.findById(id);
    }

    @Loggable
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody CreateProductRequest request) {
        log.info("New request: POST /products. Creating product [name = {}, price = {}].",
                request.name(), request.price());
        log.debug("Request to create product: {}.", request);
        return service.create(request);
    }

    @Loggable
    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable Long id,
                             @Valid @RequestBody UpdateProductRequest request) {
        log.info("New request: PATCH /products/{id}.", id);
        log.debug("Request to update product fields: {}.", request);
        return service.update(id, request);
    }
}
