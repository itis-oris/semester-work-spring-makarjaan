package com.makarova.service.impl;

import com.makarova.entity.ApartmentPhoto;
import com.makarova.repository.ApartmentPhotoRepository;
import com.makarova.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final ApartmentPhotoRepository apartmentPhotoRepository;


    @Override
    public List<String> getPhotosByApartmentId(Long apartId, String dealType) {
        List<ApartmentPhoto> photos = apartmentPhotoRepository.findByApartmentIdAndDealType(apartId, dealType);

        return photos.stream()
                .map(ApartmentPhoto::getPhotoUrl)
                .toList();
    }
}