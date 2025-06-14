package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.FavoriteService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;
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
                           Model model, Authentication authentication,
                           Principal principal) {

        ApartmentDto apartment;

        apartment = apartmentService.getApartmentInfo(apartmentId);
        boolean isFavorite = false;

        Long apartId = apartment.getId();
        List<String> photoUrl = photoService.getPhotosByApartmentId(apartId, dealType);

        if (principal != null) {
            UserDto userDto = userService.findByEmail(principal.getName());
            isFavorite = Boolean.parseBoolean(Boolean.toString(favoriteService.isApartmentInFavorites(
                    userDto.getId(),
                    apartId,
                    dealType
            )));
        }

        boolean isAdmin = false;
        boolean isUser = false;

        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            isAdmin = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            isUser = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("apartment", apartment);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("apartmnetPhoto", photoUrl);
        model.addAttribute("type", dealType);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        return "detail";
    }
}