package com.makarova.restControllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
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
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            Principal principal) {

        try {
            Map<String, String> errors = new HashMap<>();

            if (images == null || images.length == 0 ||
                    Arrays.stream(images).allMatch(MultipartFile::isEmpty)) {
                errors.put("images", "Добавьте хотя бы одну фотографию");
            }


            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("errors", errors));
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

