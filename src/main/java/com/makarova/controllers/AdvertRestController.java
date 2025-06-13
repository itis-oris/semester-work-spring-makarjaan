package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/adverts")
@RequiredArgsConstructor
public class AdvertRestController {

    private final ApartmentService apartmentService;

    @PostMapping("/add")
    public ResponseEntity<?> createAdvert(
            @Valid @ModelAttribute ApartmentDto apartmentDto,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            Principal principal) {

        try {

            if (images == null || images.length == 0 || images[0].isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("errors", Map.of("mainPhotoUrl", "Добавьте хотя бы одну фотографию")));
            }

            String userEmail = "hui@ru";
            apartmentService.saveAdvert(userEmail, apartmentDto, images);
            return ResponseEntity.ok().body(Map.of("message", "Объявление успешно добавлено"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Произошла ошибка при добавлении объявления: " + e.getMessage()));
        }
    }
}

