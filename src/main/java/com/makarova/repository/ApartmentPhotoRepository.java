package com.makarova.repository;

import com.makarova.entity.ApartmentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentPhotoRepository extends JpaRepository<ApartmentPhoto, Long> {

    @Query("SELECT p FROM ApartmentPhoto p WHERE p.apartment.id = :id AND p.apartment.dealType = :dealType")
    List<ApartmentPhoto> findByApartmentIdAndDealType(
            @Param("id") Long id,
            @Param("dealType") String dealType
    );


}