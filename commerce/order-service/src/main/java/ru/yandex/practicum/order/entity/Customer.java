package ru.yandex.practicum.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_email")
    private String email;
}
