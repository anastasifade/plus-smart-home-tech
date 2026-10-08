package ru.yandex.practicum.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String name;

    @Column(length = 500)
    String description;

    @Override
    public String toString() {
        return "Category{" +
                (id != null ? "id=" + id : "") +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
