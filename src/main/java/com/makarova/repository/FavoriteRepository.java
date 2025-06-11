package com.makarova.repository;

import com.makarova.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUser_IdAndApartmentIdAndDealType(Long userId, Long apartmentId, String dealType);
    boolean existsByUser_IdAndApartmentIdAndDealType(Long userId, Long apartmentId, String dealType);
}