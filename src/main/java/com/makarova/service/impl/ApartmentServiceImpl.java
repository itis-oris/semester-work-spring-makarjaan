package com.makarova.service.impl;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.Apartment;
import com.makarova.entity.ApartmentPhoto;
import com.makarova.entity.Role;
import com.makarova.entity.User;
import com.makarova.repository.ApartmentPhotoRepository;
import com.makarova.repository.ApartmentRepository;
import com.makarova.service.ApartmentService;
import com.makarova.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentPhotoRepository apartmentPhotoRepository;

    @Override
    public List<ApartmentDto> getByUserIdAndDealType(Long userId, String dealType) {
        List<Apartment> apartmentDtos = apartmentRepository.findByUser_IdAndDealType(userId, dealType);
        return apartmentDtos.stream()
                .map(ApartmentDto::from)
                .toList();
    }

    @Override
    public void saveAdvert(UserDto userDto, ApartmentDto apartmentDto, MultipartFile[] images) {

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phone(userDto.getPhone())
                .hashPassword(userDto.getPassword())
                .build();

        Apartment apartment = Apartment.builder()
                .title(apartmentDto.getTitle())
                .description(apartmentDto.getDescription())
                .typeOfApartment(apartmentDto.getTypeOfApartment())
                .roomsCount(apartmentDto.getRoomsCount())
                .area(apartmentDto.getArea())
                .address(apartmentDto.getAddress())
                .dealType(apartmentDto.getDealType())
                .priceSale(apartmentDto.getPriceSale())
                .priceRent(apartmentDto.getPriceRent())
                .typeOfRent(apartmentDto.getTypeOfRent())
                .status("Опубликовано")
                .user(user)
                .build();

        apartment = apartmentRepository.save(apartment);

//        for (MultipartFile image : images) {
//            if (!image.isEmpty()) {
//                try {
//                    File tempFile = ImageUtil.makeTempFile(image);
//                    String photoUrl = CloudinaryUtil.upload(tempFile, "apartment/" + apartment.getId());
//
//                    ApartmentPhoto photo = ApartmentPhoto.builder()
//                            .apartment(apartment)
//                            .photoUrl(photoUrl)
//                            .typeOfAdvert(apartment.getDealType())
//                            .build();
//
//                    apartmentPhotoRepository.save(photo);
//                } catch (IOException e) {
//                    throw new RuntimeException("Ошибка при обработке фото: " + image.getOriginalFilename(), e);
//                }
//            }
//        }
    }
}
