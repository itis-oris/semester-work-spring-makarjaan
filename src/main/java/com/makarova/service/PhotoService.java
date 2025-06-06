package com.makarova.service;

import java.util.List;

public interface PhotoService {

    List<String> getPhotosByApartmentId(Long apartId, String dealType);
}