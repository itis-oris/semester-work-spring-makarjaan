package com.makarova.exception;

import com.makarova.dto.ApartmentDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ApartmentDtoValidator implements ConstraintValidator<ValidApartmentDto, ApartmentDto> {

    @Override
    public boolean isValid(ApartmentDto dto, ConstraintValidatorContext ctx) {
        boolean valid = true;

        if ("sale".equals(dto.getDealType())) {
            if (dto.getPriceSale() == null || dto.getPriceSale() <= 0) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate("Укажите цену продажи")
                        .addPropertyNode("priceSale").addConstraintViolation();
                valid = false;
            }
        } else if ("rent".equals(dto.getDealType())) {
            if (dto.getPriceRent() == null || dto.getPriceRent() <= 0) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate("Укажите цену аренды")
                        .addPropertyNode("priceRent").addConstraintViolation();
                valid = false;
            }

            if (dto.getTypeOfRent() == null || dto.getTypeOfRent().isBlank()) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate("Укажите тип аренды")
                        .addPropertyNode("typeOfRent").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
