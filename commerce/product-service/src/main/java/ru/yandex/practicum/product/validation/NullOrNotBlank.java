package ru.yandex.practicum.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {
    String message() default "The field must be either null, or have a non-blank value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}