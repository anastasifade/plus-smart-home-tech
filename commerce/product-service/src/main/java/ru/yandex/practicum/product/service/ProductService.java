package ru.yandex.practicum.product.service;

import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.UpdateProductRequest;

import java.util.List;
import java.util.Set;

public interface ProductService {
    Set<ProductDto> findAll();

    Set<ProductDto> findAllByCategory(Long categoryId);

    Set<ProductDto> search(String query);

    Set<ProductDto> findAllByIds(Set<Long> ids);

    ProductDto findById(Long id);

    ProductDto create(CreateProductRequest newProduct);

    ProductDto update(Long id, UpdateProductRequest updateRequest);
}
