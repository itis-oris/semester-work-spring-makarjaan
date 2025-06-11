package com.makarova.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {

    List<String> getPhotosByApartmentId(Long apartId, String dealType);

    String uploadPhoto(MultipartFile file);
}