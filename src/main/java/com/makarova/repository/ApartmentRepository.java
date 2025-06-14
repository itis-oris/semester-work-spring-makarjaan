package com.makarova.repository;

import com.makarova.entity.Apartment;
import com.makarova.entity.ApartmentStatus;
import com.makarova.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    List<Apartment> findByUser_IdAndDealType(Long userId, String dealType);

    Optional<Apartment> findByIdAndUserEmail(Long id, String email);

    void deleteApartmentByUser(User user);

    @Modifying
    @Query("UPDATE Apartment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    default List<Apartment> getRent(String typeRent) {
        return findByDealTypeAndTypeOfRent(typeRent);
    }

    List<Apartment> findByDealTypeAndStatus(String dealType, ApartmentStatus status);

    @Query("SELECT a FROM Apartment a WHERE a.dealType = 'rent' AND a.typeOfRent = :typeRent")
    List<Apartment> findByDealTypeAndTypeOfRent(@Param("typeRent") String typeRent);

    @Query(value = """
        SELECT * FROM apartment a
        WHERE (:dealType IS NULL OR a.deal_type = :dealType)
          AND (:rentType IS NULL OR a.type_of_rent = :rentType)
          AND (:priceMin IS NULL OR (a.price_sale >= :priceMin OR a.price_rent >= :priceMin))
          AND (:priceMax IS NULL OR (a.price_sale <= :priceMax OR a.price_rent <= :priceMax))
          AND (:address IS NULL OR a.address::text ILIKE CONCAT('%', :address, '%'))
          AND (:rooms IS NULL OR a.rooms_count = :rooms)
          AND (:propertyType IS NULL OR a.type_of_apartment = :propertyType)
          AND a.status = 'PUBLISHED'
        """, nativeQuery = true)
    List<Apartment> findApartmentsByFilter(
            @Param("dealType") String dealType,
            @Param("rentType") String rentType,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("address") String address,
            @Param("rooms") String rooms,
            @Param("propertyType") String propertyType);

    @Query("SELECT a FROM Apartment a WHERE a.status = 'PUBLISHED'")
    List<Apartment> findAllPublished();

}
