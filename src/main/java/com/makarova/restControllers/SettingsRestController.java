package com.makarova.restControllers;


import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SettingsRestController {

    private final UserService userService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/api/settings")
    public ResponseEntity<?> handleSettingsAction(
            @RequestPart("action") String action,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(name = "profilePhoto", required = false) MultipartFile profilePhoto,
            @RequestParam(name = "changeUserName", required = false) String username,
            @RequestParam(name = "messengers", required = false) String messengers,
            @RequestParam(name = "currentPassword", required = false) String currentPassword,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "addvertApartIdForUnpublish", required = false) Long apartmentIdUnpublish,
            @RequestParam(name = "addvertApartIdForDelete", required = false) Long apartmentIdDelete,
            @RequestParam(name = "dealType", required = false) String dealType,
            @RequestParam(name = "status", required = false) String status) throws IOException {

        UserDto user = userService.findByEmail("hui@ru");

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
                // apartmentService.changeStatus(apartmentIdUnpublish, ApartmentStatus.valueOf(status));
                return ResponseEntity.ok().build();

            case "deleteApartment":
                // apartmentService.deleteApartment(apartmentIdDelete);
                return ResponseEntity.ok().build();

            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Неизвестное действие"));
        }
    }

}