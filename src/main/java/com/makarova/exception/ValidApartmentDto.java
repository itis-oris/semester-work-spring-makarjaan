package com.makarova.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ApartmentDtoValidator.class)
public @interface ValidApartmentDto {
    String message() default "Некорректные поля объявления";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
