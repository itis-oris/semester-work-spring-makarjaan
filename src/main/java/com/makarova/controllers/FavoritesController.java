package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.Apartment;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import com.makarova.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FavoritesController {

    private final ApartmentService apartmentService;
    private final UserService userService;

    public FavoritesController(ApartmentService apartmentService, UserService userService) {
        this.apartmentService = apartmentService;
        this.userService = userService;
    }


    @GetMapping("/favorites")
    public String getFavoritesPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/signIn";
        }

        UserDto userDto = userService.findByEmail(principal.getName());

        List<ApartmentDto> favorites = apartmentService.getFavoriteApartmentsByEmail(userDto.getEmail());

        List<ApartmentDto> listSale = favorites.stream()
                .filter(a -> "sale".equalsIgnoreCase(a.getDealType()))
                .collect(Collectors.toList());

        List<ApartmentDto> listRent = favorites.stream()
                .filter(a -> "rent".equalsIgnoreCase(a.getDealType()))
                .collect(Collectors.toList());

        model.addAttribute("list", favorites);
        model.addAttribute("listSale", listSale);
        model.addAttribute("listRent", listRent);

        return "favorites";
    }

}
