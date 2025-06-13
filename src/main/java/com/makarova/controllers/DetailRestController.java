package com.makarova.controllers;

import com.makarova.dto.UserDto;
import com.makarova.service.FavoriteService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
public class DetailRestController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    @PostMapping("/favorite")
    public ResponseEntity<String> toggleFavorite(
            @RequestParam("action") String action,
            @RequestParam("apartmentId") Long apartmentId,
            @RequestParam("dealType") String dealType,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.ok("false");
        }

        UserDto userDto = userService.findByEmail("hui@ru");
        boolean isFavorite = favoriteService.toggleFavorite(
            userDto.getId(),
            apartmentId,
            dealType
        );

        return ResponseEntity.ok(Boolean.toString(isFavorite));
    }
}