package com.makarova.repository;

import com.makarova.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsername(@Param("id") Long id, @Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.messengers = :messengers WHERE u.id = :id")
    void updateMessengers(@Param("id") Long id, @Param("messengers") String messengers);

    @Modifying
    @Query("UPDATE User u SET u.phone = :phone WHERE u.id = :id")
    void updatePhone(@Param("id") Long id, @Param("phone") String phone);

    @Modifying
    @Query("UPDATE User u SET u.profilePhotoUrl = :profilePhotoUrl WHERE u.id = :id")
    void updateProfilePhotoUrl(@Param("id") Long id, @Param("profilePhotoUrl") String profilePhotoUrl);
}
