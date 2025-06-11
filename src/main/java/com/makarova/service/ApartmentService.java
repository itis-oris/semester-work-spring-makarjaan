package com.makarova.service;

import com.makarova.dto.ApartmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {
    List<ApartmentDto> getByUserIdAndDealType(Long userId, String dealType);

    void saveAdvert(String userEmail, ApartmentDto apartmentDto, MultipartFile[] file);

    ApartmentDto getApartmentInfo(Long apartmentId);
}