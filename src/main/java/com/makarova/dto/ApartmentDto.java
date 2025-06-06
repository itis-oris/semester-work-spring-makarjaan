package com.makarova.dto;

import com.makarova.entity.Apartment;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentDto {

    private Long id;

    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotBlank(message = "Тип недвижимости не может быть пустым")
    private String typeOfApartment;

    @NotBlank(message = "Количество комнат не может быть пустым")
    private String roomsCount;

    @NotNull(message = "Площадь не может быть пустой")
    @DecimalMin(value = "0.01", message = "Площадь должна быть больше 0")
    private Double area;

    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @NotBlank(message = "Тип сделки не может быть пустым")
    private String dealType;

    private Integer priceSale;

    private Integer priceRent;

    private String typeOfRent;

    @NotBlank(message = "Статус не может быть пустым")
    private String status;

    private Boolean isFavorite;

    private String mainPhotoUrl;

    public static ApartmentDto from(Apartment apartment) {
        return ApartmentDto.builder()
                .id(apartment.getId())
                .title(apartment.getTitle())
                .description(apartment.getDescription())
                .typeOfApartment(apartment.getTypeOfApartment())
                .roomsCount(apartment.getRoomsCount())
                .area(apartment.getArea())
                .address(apartment.getAddress())
                .dealType(apartment.getDealType())
                .priceSale(apartment.getPriceSale())
                .priceRent(apartment.getPriceRent())
                .typeOfRent(apartment.getTypeOfRent())
                .status(apartment.getStatus())
                .build();
    }
}