package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.ApartmentStatus;
import com.makarova.entity.Apartment;
import com.makarova.service.ApartmentService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SettingsRestController {

    private final UserService userService;
    private final ApartmentService apartmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/api/settings")
    public ResponseEntity<?> handleSettingsAction(
            @RequestParam("action") String action,
            Principal principal,
            @RequestPart(name = "profilePhoto", required = false) MultipartFile profilePhoto,
            @RequestParam(name = "changeUserName", required = false) String username,
            @RequestParam(name = "messengers", required = false) String messengers,
            @RequestParam(name = "currentPassword", required = false) String currentPassword,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "apartmentId", required = false) Long apartmentId,
            @RequestParam(name = "dealType", required = false) String dealType) throws IOException {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован");
        }

        UserDto user = userService.findByEmail(principal.getName());

        switch (action) {
            case "uploadPhoto":
                if (profilePhoto != null && !profilePhoto.isEmpty()) {
                    userService.updateProfilePhoto(user, profilePhoto);
                }
                if (username != null && !username.trim().isEmpty()) {
                    user.setUsername(username);
                }
                if (messengers != null) {
                    user.setMessengers(messengers);
                }
                userService.updateUserInfo(user);
                return ResponseEntity.ok().build();

            case "deletePhoto":
                userService.deleteProfilePhoto(user);
                return ResponseEntity.ok().build();

            case "changePassword":
                if (currentPassword == null || newPassword == null ||
                        !userService.changePassword(user, currentPassword, newPassword)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Неверный текущий пароль"));
                }
                return ResponseEntity.ok().build();

            case "deleteAccount":
                userService.deleteAccount(user);
                return ResponseEntity.ok().build();

            case "unpublishApartment":
                if (apartmentId != null) {
                    ApartmentDto apartment = apartmentService.getApartmentInfo(apartmentId);
                    if (apartment.getStatus() == ApartmentStatus.COMPLETED) {
                        apartmentService.changeStatus(apartmentId, ApartmentStatus.PUBLISHED);
                    } else {
                        apartmentService.changeStatus(apartmentId, ApartmentStatus.COMPLETED);
                    }
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.badRequest().body(Map.of("error", "ID квартиры не указан"));

            case "deleteApartment":
                if (apartmentId != null) {
                    apartmentService.deleteApartment(apartmentId);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.badRequest().body(Map.of("error", "ID квартиры не указан"));

            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Неизвестное действие"));
        }
    }
}