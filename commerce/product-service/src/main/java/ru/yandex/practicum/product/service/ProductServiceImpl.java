package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.common.CategoryDto;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.CategoryMapper;
import ru.yandex.practicum.product.mapper.ProductMapper;
import ru.yandex.practicum.product.repository.ProductRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository repository;

    @Override
    public Set<ProductDto> findAll() {
        log.debug("Product service: request to find all products.");
        return toDto(repository.findByActive(true));

    }

    @Override
    public Set<ProductDto> findAllByCategory(Long categoryId) {
        log.debug("Product service: request to find product in category [id = {}].", categoryId);

        CategoryDto category = categoryService.findById(categoryId);
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(product -> ProductMapper.toDto(product, category))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ProductDto> search(String query) {
        log.debug("Product service: request to search for products by name containing: \"{}\".", query);
        if (query.isBlank()) {
            return Set.of();
        }
        return toDto(repository.findByNameContainingIgnoreCase(query));
    }

    @Override
    public Set<ProductDto> findAllByIds(Set<Long> ids) {
        log.debug("Product service: request to find all products with ids in: {}.", ids);
        return toDto(repository.findAllById(ids));
    }

    @Override
    public ProductDto findById(Long id) {
        log.debug("Product service: request to find product [id = {}].", id);
        Product product = findProductById(id);
        return toDto(product);
    }

    @Transactional
    @Override
    public ProductDto create(CreateProductRequest newProduct) {
        log.debug("Product service: request to create new product.");

        Category category = newProduct.categoryId() != null ?
                categoryService.findCategoryById(newProduct.categoryId()) : null;
        Product product = ProductMapper.toProduct(newProduct, category);

        repository.save(product);

        log.info("New product created. Id = {}.", product.getId());
        return toDto(product);
    }

    @Override
    @Transactional
    public ProductDto update(Long id, UpdateProductRequest updateRequest) {
        log.debug("Product service: request to update product [id = {}].", id);

        Product product = findProductById(id);
        Category category = updateRequest.categoryId() != null ?
                categoryService.findCategoryById(updateRequest.categoryId()) : null;

        ProductMapper.updateProduct(product, updateRequest, category);
        repository.save(product);

        log.info("Product [id = {}] updated.", id);
        return toDto(product);
    }

    private Product findProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product [id = %d] not found.", id)));
    }

    private ProductDto toDto(Product product) {
        return ProductMapper.toDto(product,
                product.getCategory() != null ? CategoryMapper.toDto(product.getCategory()) : null);
    }

    private Set<ProductDto> toDto(Collection<Product> products) {
        return products
                .stream()
                .map(product -> ProductMapper.toDto(product,
                        product.getCategory() != null ? CategoryMapper.toDto(product.getCategory()) : null))
                .collect(Collectors.toSet());
    }
}
