package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;
    private final ApartmentService apartmentService;
    private final PhotoService photoService;


    @GetMapping("/settings")
    public String getSettingsPage(Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }

        String email = "hui@ru";
        UserDto user = userService.findByEmail(email);

        List<ApartmentDto> apartmentsSale = apartmentService.getByUserIdAndDealType(user.getId(), "sale");
        List<ApartmentDto> apartmentsRent = apartmentService.getByUserIdAndDealType(user.getId(), "rent");

        List<String> mainPhotosSale = apartmentsSale.stream()
                .map(apt -> {
                    List<String> photos = photoService.getPhotosByApartmentId(apt.getId(), "sale");
                    return photos.isEmpty() ? null : photos.getFirst();
                })
                .filter(Objects::nonNull)
                .toList();

        List<String> mainPhotosRent = apartmentsRent.stream()
                .map(apt -> {
                    List<String> photos = photoService.getPhotosByApartmentId(apt.getId(), "rent");
                    return photos.isEmpty() ? null : photos.getFirst();
                })
                .filter(Objects::nonNull)
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("apartmentsSale", apartmentsSale);
        model.addAttribute("apartmentsRent", apartmentsRent);
        model.addAttribute("mainPhotosSale", mainPhotosSale);
        model.addAttribute("mainPhotosRent", mainPhotosRent);

        return "settings_page";
    }
}
