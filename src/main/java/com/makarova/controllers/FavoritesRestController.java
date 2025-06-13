package com.makarova.controllers;

import com.makarova.service.ApartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesRestController {

    private final ApartmentService apartmentService;

    public FavoritesRestController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping("/{apartmentId}/toggle")
    public ResponseEntity<Boolean> toggleFavorite(@PathVariable Long apartmentId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getName();

        boolean isFavoriteNow = apartmentService.toggleFavoriteStatus(apartmentId, email);
        return ResponseEntity.ok(isFavoriteNow);
    }
}
