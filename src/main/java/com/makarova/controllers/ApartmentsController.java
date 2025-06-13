package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.ApartmentFilterDto;
import com.makarova.entity.Apartment;
import com.makarova.service.ApartmentService;
import com.makarova.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ApartmentsController {

    private final ApartmentService apartmentService;

    @GetMapping("/apartments")
    public String showApartments(Model model) {
        model.addAttribute("filter", new ApartmentFilterDto());
        model.addAttribute("apartmentsSale", List.of());
        model.addAttribute("apartmentsShortRent", List.of());
        model.addAttribute("apartmentsLongRent", List.of());
        return "apartments";
    }



    @PostMapping("/apartments")
    public String filterApartments(@ModelAttribute ApartmentFilterDto filter, Model model) {
        model.addAttribute("filter", filter);

        List<ApartmentDto> apartments = apartmentService.findApartmentsByFilter(filter);

        if ("Продажа".equalsIgnoreCase(filter.getDealType())) {
            model.addAttribute("apartmentsSale", apartments);
            model.addAttribute("apartmentsShortRent", List.of());
            model.addAttribute("apartmentsLongRent", List.of());
        } else if ("Аренда".equalsIgnoreCase(filter.getDealType())) {
            if ("Посуточно".equalsIgnoreCase(filter.getRentType())) {
                model.addAttribute("apartmentsSale", List.of());
                model.addAttribute("apartmentsShortRent", apartments);
                model.addAttribute("apartmentsLongRent", List.of());
            } else if ("Долгосрочно".equalsIgnoreCase(filter.getRentType())) {
                model.addAttribute("apartmentsSale", List.of());
                model.addAttribute("apartmentsShortRent", List.of());
                model.addAttribute("apartmentsLongRent", apartments);
            } else {
                model.addAttribute("apartmentsSale", List.of());
                model.addAttribute("apartmentsShortRent", List.of());
                model.addAttribute("apartmentsLongRent", List.of());
            }
        } else {
            model.addAttribute("apartmentsSale", List.of());
            model.addAttribute("apartmentsShortRent", List.of());
            model.addAttribute("apartmentsLongRent", List.of());
        }

        return "apartments";
    }


}