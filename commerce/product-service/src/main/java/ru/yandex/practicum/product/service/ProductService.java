package ru.yandex.practicum.product.service;

import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    List<ProductDto> findAllByCategory(Long categoryId);

    List<ProductDto> search(String query);

    ProductDto findById(Long id);

    ProductDto create(CreateProductRequest newProduct);

    ProductDto update(Long id, UpdateProductRequest updateRequest);
}
