package ru.yandex.practicum.product.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;

@UtilityClass
public class CategoryMapper {

    public Category toCategory(CreateCategoryRequest request) {
        String name = request.name().trim();
        String description = request.description() != null ? request.description().trim() : null;

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    public CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

}
