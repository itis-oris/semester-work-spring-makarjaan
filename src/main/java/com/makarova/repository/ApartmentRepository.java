package com.makarova.repository;

import com.makarova.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment> findByUser_Id(Long userId);

    @Query("SELECT a.createdAt FROM Apartment a WHERE a.id = :id")
    Timestamp getCreatedTime(@Param("id") Long id);

    Optional<Apartment> findById(Long id);

    List<Apartment> findByUser_IdAndDealType(Long userId, String dealType);

    @Modifying
    @Query("UPDATE Apartment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    default List<Apartment> getRent(String typeRent) {
        return findByDealTypeAndTypeOfRent(typeRent);
    }

    @Query("SELECT a FROM Apartment a WHERE a.dealType = 'rent' AND a.typeOfRent = :typeRent")
    List<Apartment> findByDealTypeAndTypeOfRent(@Param("typeRent") String typeRent);

    default List<Apartment> filterApartmentsSale(
            BigDecimal priceMin, BigDecimal priceMax, String address, String rooms, String propertyType) {
        return filterApartments("sale", priceMin, priceMax, address, rooms, propertyType);
    }

    default List<Apartment> filterApartmentsRent(
            String type, BigDecimal priceMin, BigDecimal priceMax, String address, String rooms, String propertyType) {
        return filterApartments("rent", priceMin, priceMax, address, rooms, propertyType);
    }

    @Query("SELECT a FROM Apartment a WHERE a.dealType = :dealType " +
            "AND (:priceMin IS NULL OR (a.priceSale BETWEEN :priceMin AND :priceMax OR a.priceRent BETWEEN :priceMin AND :priceMax)) " +
            "AND (:address IS NULL OR a.address LIKE %:address%) " +
            "AND (:rooms IS NULL OR a.roomsCount = :rooms) " +
            "AND (:propertyType IS NULL OR a.typeOfApartment = :propertyType)")
    List<Apartment> filterApartments(
            @Param("dealType") String dealType,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType);


}
