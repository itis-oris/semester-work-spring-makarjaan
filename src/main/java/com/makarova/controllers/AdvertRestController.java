package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/adverts")
@RequiredArgsConstructor
public class AdvertRestController {

    private final ApartmentService apartmentService;
    private final PhotoService photoService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> createAdvert(
            @Valid @ModelAttribute ApartmentDto apartmentDto,
            BindingResult bindingResult,
            @RequestParam("images") MultipartFile[] images,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            String userEmail = principal.getName();
            UserDto user = userService.findByEmail(userEmail);

            apartmentService.saveAdvert(user, apartmentDto, images);
            return ResponseEntity.ok().body(Map.of("message", "Объявление успешно добавлено"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Произошла ошибка при добавлении объявления"));
        }
    }
}