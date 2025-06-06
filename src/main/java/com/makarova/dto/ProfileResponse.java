package com.makarova.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private UserDto user;
    private List<ApartmentDto> apartmentsSale;
    private List<ApartmentDto> apartmentsRent;
}