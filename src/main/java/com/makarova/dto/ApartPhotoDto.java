package com.makarova.dto;

import com.makarova.entity.ApartmentPhoto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartPhotoDto {

    private Long id;
    private String photoUrl;
    private String typeOfAdvert;
    private Long apartmentId;

    public static ApartPhotoDto from(ApartmentPhoto entity) {
        return ApartPhotoDto.builder()
                .id(entity.getId())
                .photoUrl(entity.getPhotoUrl())
                .typeOfAdvert(entity.getTypeOfAdvert())
                .build();
    }
}