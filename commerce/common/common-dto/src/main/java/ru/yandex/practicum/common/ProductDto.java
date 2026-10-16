package ru.yandex.practicum.common;

import java.math.BigDecimal;

public record ProductDto(

        Long id,

        String name,

        String description,

        BigDecimal price,

        CategoryDto category,

        String imageUrl,

        Boolean active
) {
}
