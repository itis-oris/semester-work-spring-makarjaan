package com.makarova.service;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.ApartmentFilterDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.Apartment;
import com.makarova.entity.ApartmentStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {
    List<ApartmentDto> getByUserIdAndDealType(Long userId, String dealType);

    void saveAdvert(String userEmail, ApartmentDto apartmentDto, MultipartFile[] file);

    ApartmentDto getApartmentInfo(Long apartmentId);

    void deleteByUser(UserDto user);

    void changeStatus(Long id, ApartmentStatus newStatus);

    void deleteApartment(Long id);

    List<ApartmentDto> getFavoriteApartmentsByEmail(String email);

    boolean toggleFavoriteStatus(Long apartmentId, String userEmail, String dealType);

    List<ApartmentDto> findApartmentsForRentAdmin();

    List<ApartmentDto> findApartmentsForSaleAdmin();

    List<ApartmentDto> findApartmentsByFilter(ApartmentFilterDto filter);
}