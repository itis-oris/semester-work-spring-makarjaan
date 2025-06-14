package com.makarova.controllers.rest;

import com.makarova.dto.UserDto;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> handleRegistration(
            @Valid @RequestBody UserDto userDto) throws AuthException {
        userService.register(userDto);
        return ResponseEntity.ok().body(Map.of("message", "Регистрация успешна"));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        UserDto user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(Map.of(
            "username", user.getUsername(),
            "email", user.getEmail(),
            "profilePhotoUrl", user.getProfilePhotoUrl(),
            "messengers", user.getMessengers()
        ));
    }
}