package com.makarova.service;

public interface FavoriteService {
    boolean isApartmentInFavorites(Long userId, Long apartmentId, String dealType);

    boolean toggleFavorite(Long userId, Long apartmentId, String dealType);
}