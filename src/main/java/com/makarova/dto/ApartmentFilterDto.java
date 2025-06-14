package com.makarova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentFilterDto {
    private Integer priceMin;
    private Integer priceMax;
    private String address;
    private String rooms;
    private String dealType;  // продажа или аренда
    private String rentType;  // посуточно или долгосрочно
    private String propertyType;
}