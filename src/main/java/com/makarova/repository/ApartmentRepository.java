package com.makarova.repository;

import com.makarova.entity.Apartment;
import com.makarova.entity.ApartmentStatus;
import com.makarova.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment> findByUser_IdAndDealType(Long userId, String dealType);

    void deleteApartmentByUser(User user);

    @Modifying
    @Query("UPDATE Apartment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    List<Apartment> findByDealTypeAndStatus(String dealType, ApartmentStatus status);

    @Query("SELECT a FROM Apartment a WHERE a.dealType = 'rent' AND a.typeOfRent = :typeRent")
    List<Apartment> findByDealTypeAndTypeOfRent(@Param("typeRent") String typeRent);

    @Query("SELECT a FROM Apartment a WHERE " +
            "(:dealType IS NULL OR a.dealType = :dealType) AND " +
            "(:rentType IS NULL OR a.typeOfRent = :rentType) AND " +
            "(:priceMin IS NULL OR (a.dealType = 'sale' AND a.priceSale >= :priceMin) OR (a.dealType = 'rent' AND a.priceRent >= :priceMin)) AND " +
            "(:priceMax IS NULL OR (a.dealType = 'sale' AND a.priceSale <= :priceMax) OR (a.dealType = 'rent' AND a.priceRent <= :priceMax)) AND " +
            "(:address IS NULL OR a.address LIKE %:address%) AND " +
            "(:rooms IS NULL OR a.roomsCount = :rooms) AND " +
            "(:propertyType IS NULL OR a.typeOfApartment = :propertyType)")
    List<Apartment> findApartmentsByFilter(
            @Param("dealType") String dealType,
            @Param("rentType") String rentType,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType
    );

}
