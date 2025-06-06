package com.makarova.service;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {

    List<ApartmentDto> getByUserIdAndDealType(Long userId, String dealType);

    void saveAdvert(UserDto userDto, ApartmentDto apartmentDto, MultipartFile[] file);
}
