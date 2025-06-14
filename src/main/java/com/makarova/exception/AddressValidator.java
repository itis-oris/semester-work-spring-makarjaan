package com.makarova.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<ValidAddress, String> {
    @Override
    public boolean isValid(String address, ConstraintValidatorContext context) {
        return address != null && !address.isBlank() && !"Не выбран".equals(address);
    }
}
