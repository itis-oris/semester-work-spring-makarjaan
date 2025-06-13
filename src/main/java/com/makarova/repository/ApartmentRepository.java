package com.makarova.repository;

import com.makarova.entity.Apartment;
import com.makarova.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment> findByUser_IdAndDealType(Long userId, String dealType);

    void deleteApartmentByUser(User user);

    @Modifying
    @Query("UPDATE Apartment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    default List<Apartment> getRent(String typeRent) {
        return findByDealTypeAndTypeOfRent(typeRent);
    }

    @Query("SELECT a FROM Apartment a WHERE a.dealType = 'rent' AND a.typeOfRent = :typeRent")
    List<Apartment> findByDealTypeAndTypeOfRent(@Param("typeRent") String typeRent);

    @Query("SELECT a FROM Apartment a " +
            "WHERE a.dealType = 'sale' " +
            "AND (:priceMin IS NULL OR a.priceSale >= :priceMin) " +
            "AND (:priceMax IS NULL OR a.priceSale <= :priceMax) " +
            "AND (:address IS NULL OR LOWER(a.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "AND (:rooms IS NULL OR a.roomsCount = :rooms) " +
            "AND (:propertyType IS NULL OR a.typeOfApartment = :propertyType) " +
            "AND a.status = 'Опубликовано'")
    List<Apartment> findBySaleFilter(
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType);

    @Query("SELECT a FROM Apartment a " +
            "WHERE a.dealType = 'rent' " +
            "AND (:typeOfRent IS NULL OR a.typeOfRent = :typeOfRent) " +
            "AND (:priceMin IS NULL OR a.priceRent >= :priceMin) " +
            "AND (:priceMax IS NULL OR a.priceRent <= :priceMax) " +
            "AND (:address IS NULL OR LOWER(a.address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "AND (:rooms IS NULL OR a.roomsCount = :rooms) " +
            "AND (:propertyType IS NULL OR a.typeOfApartment = :propertyType) " +
            "AND a.status = 'Опубликовано'")
    List<Apartment> findByRentFilter(
            @Param("typeOfRent") String typeOfRent,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType);


    @Query("""
        select a from Apartment a
        where (:dealType is null or a.dealType = :dealType)
          and (:rentType is null or a.typeOfRent = :rentType)
          and (:priceMin is null or (a.priceSale >= :priceMin or a.priceRent >= :priceMin))
          and (:priceMax is null or (a.priceSale <= :priceMax or a.priceRent <= :priceMax))
          and (:address is null or lower(a.address) like lower(concat('%', :address, '%')))
          and (:rooms is null or a.roomsCount = :rooms)
          and (:propertyType is null or a.typeOfApartment = :propertyType)
          and a.status = 'Опубликовано'
        """)
    List<Apartment> findApartmentsByFilter(
            @Param("dealType") String dealType,
            @Param("rentType") String rentType,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType);

}
