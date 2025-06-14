package com.makarova.service.impl;

import com.makarova.entity.ApartmentPhoto;
import com.makarova.repository.ApartmentPhotoRepository;
import com.makarova.service.PhotoService;
import com.makarova.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final ApartmentPhotoRepository apartmentPhotoRepository;
    private final CloudinaryUtil cloudinaryUtil;

    @Override
    public List<String> getPhotosByApartmentId(Long apartId, String dealType) {
        List<ApartmentPhoto> photos = apartmentPhotoRepository.findByApartmentIdAndDealType(apartId, dealType);

        return photos.stream()
                .map(ApartmentPhoto::getPhotoUrl)
                .toList();
    }

    @Override
    public String uploadPhoto(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload-", ".jpg");
            file.transferTo(tempFile);

            String photoUrl = cloudinaryUtil.upload(tempFile, "apartment/temp");
            tempFile.delete();

            return photoUrl;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке фото: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public String getMainPhoto(Long apartId, String dealType) {
        List<ApartmentPhoto> photos = apartmentPhotoRepository.findByApartmentIdAndDealType(apartId, dealType);
        if (photos.isEmpty()) {
            return "/static/img/no-photo.png";
        }
        return photos.get(0).getPhotoUrl();
    }
}