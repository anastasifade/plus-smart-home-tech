package ru.yandex.practicum.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(precision = 19, scale = 4)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean active;

    @Override
    public String toString() {
        return "Product{" +
                (id != null ? "id=" + id : "") +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                (category != null ? ", category id=" + category.getId() : "") +
                ", imageUrl='" + imageUrl + '\'' +
                ", active=" + (active != null ? active : "") +
                '}';
    }
}
