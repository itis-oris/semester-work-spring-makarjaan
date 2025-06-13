package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.FavoriteService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/details")
@RequiredArgsConstructor
public class DetailController {

    private final ApartmentService apartmentService;
    private final PhotoService photoService;
    private final FavoriteService favoriteService;
    private final UserService userService;

    @GetMapping
    public String getDetails(@RequestParam("id") Long apartmentId,
                           @RequestParam("type") String dealType,
                           Model model,
                           Principal principal) {
        if (principal == null) {
            return "redirect:/signIn";
        }
        ApartmentDto apartment;
        String isFavorite = "false";

        apartment = apartmentService.getApartmentInfo(apartmentId);

        UserDto userDto = userService.findByEmail(principal.getName());


        Long apartId = apartment.getId();
        List<String> photoUrl = photoService.getPhotosByApartmentId((long) apartId, dealType);
        
        model.addAttribute("apartmnetPhoto", photoUrl);
        model.addAttribute("type", dealType);
        // model.addAttribute("phone", userService.getUserPhone(userDto.getId()));

        isFavorite = Boolean.toString(favoriteService.isApartmentInFavorites(
            userDto.getId(),
            apartId,
            dealType
        ));

        model.addAttribute("apartment", apartment);
        model.addAttribute("isFavorite", isFavorite);

        return "detail";
    }
}