package com.makarova.service.impl;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.Apartment;
import com.makarova.entity.ApartmentPhoto;
import com.makarova.entity.ApartmentStatus;
import com.makarova.entity.User;
import com.makarova.repository.ApartmentPhotoRepository;
import com.makarova.repository.ApartmentRepository;
import com.makarova.repository.UserRepository;
import com.makarova.service.ApartmentService;
import com.makarova.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentPhotoRepository apartmentPhotoRepository;
    private final UserRepository userRepository;
    private final CloudinaryUtil cloudinaryUtil;

    @Override
    public List<ApartmentDto> getByUserIdAndDealType(Long userId, String dealType) {
        List<Apartment> apartmentDtos = apartmentRepository.findByUser_IdAndDealType(userId, dealType);
        return apartmentDtos.stream()
                .map(ApartmentDto::from)
                .toList();
    }

    @Override
    public void saveAdvert(String userEmail, ApartmentDto apartmentDto, MultipartFile[] images) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
                .status(apartmentDto.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isFavorite(false)
                .user(user)
                .build();

        apartment = apartmentRepository.save(apartment);

        boolean mainPhotoSet = false;

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    File tempFile = File.createTempFile("upload-", ".jpg");
                    image.transferTo(tempFile);

                    String photoUrl = cloudinaryUtil.upload(tempFile, "apartment/" + apartment.getId());

                    tempFile.delete();

                    ApartmentPhoto photo = ApartmentPhoto.builder()
                            .apartment(apartment)
                            .createdAt(LocalDateTime.now())
                            .photoUrl(photoUrl)
                            .typeOfAdvert(apartment.getDealType())
                            .build();

                    apartmentPhotoRepository.save(photo);

                    if (!mainPhotoSet) {
                        apartment.setMainPhotoUrl(photoUrl);
                        apartmentRepository.save(apartment);
                        mainPhotoSet = true;
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Ошибка при обработке фото: " + image.getOriginalFilename(), e);
                }
            }
        }
    }


    @Override
    public ApartmentDto getApartmentInfo(Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        return ApartmentDto.from(apartment);
    }

    @Override
    public void deleteByUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        apartmentRepository.deleteApartmentByUser(user);
    }

    @Override
    public void changeStatus(Long id, ApartmentStatus newStatus) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        apartment.setStatus(newStatus);
        apartmentRepository.save(apartment);
    }

    @Override
    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }


}
