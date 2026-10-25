package ru.yandex.practicum.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.common.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.mapper.CategoryMapper;
import ru.yandex.practicum.product.repository.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryDto> findAll() {
        log.debug("Category service: request to find all categories.");
        return repository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        log.debug("Category service: request to find category with id = {}.", id);
        return CategoryMapper.toDto(findCategoryById(id));
    }

    @Override
    public Category findCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category [id=%d] not found.", id)));
    }

    @Transactional
    @Override
    public CategoryDto create(CreateCategoryRequest newCategory) {
        log.debug("Category service: request to create new category.");
        Category category = CategoryMapper.toCategory(newCategory);

        repository.save(category);

        log.info("New category created. Id: {}.", category.getId());
        return CategoryMapper.toDto(category);
    }
}
