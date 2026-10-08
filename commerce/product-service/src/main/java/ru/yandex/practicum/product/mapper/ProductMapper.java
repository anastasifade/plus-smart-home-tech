package ru.yandex.practicum.product.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;

@UtilityClass
public class ProductMapper {

    public Product toProduct(CreateProductRequest request, Category category) {
        String name = request.name().trim();
        String description = request.description() != null ? request.description().trim() : null;
        String imageUrl = request.imageUrl() != null ? request.imageUrl().trim() : null;

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(request.price());
        product.setImageUrl(imageUrl);
        product.setActive(true);

        return product;
    }

    public Product updateProduct(Product product, UpdateProductRequest request, Category newCategory) {
         if (request.name() != null) {
             product.setName(request.name().trim());
         }

         if (request.description() != null) {
             product.setDescription(request.description().trim());
         }

         if (request.price() != null) {
             product.setPrice(request.price());
         }

         if (request.categoryId() != null && newCategory != null) {
             product.setCategory(newCategory);
         }

         if (request.imageUrl() != null) {
             product.setImageUrl(request.imageUrl().trim());
         }

         if (request.active() != null) {
             product.setActive(request.active());
         }

         return product;
    }

    public ProductDto toDto(Product product, CategoryDto category) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                category,
                product.getImageUrl(),
                product.getActive()
        );
    }
}
