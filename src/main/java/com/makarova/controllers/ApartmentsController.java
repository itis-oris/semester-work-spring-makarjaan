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
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class ApartmentsController {

    private final ApartmentService apartmentService;

    @GetMapping("/apartments")
    public String showApartments(Model model) {
        model.addAttribute("filter", new ApartmentFilterDto());

        // Получаем все опубликованные квартиры
        List<ApartmentDto> allApartments = apartmentService.findApartmentsByFilter(null);

        // Разбиваем по вкладкам
        List<ApartmentDto> apartmentsSale = allApartments.stream()
                .filter(a -> "sale".equalsIgnoreCase(a.getDealType()) && a.getStatus() != null && a.getStatus().name().equals("PUBLISHED"))
                .collect(Collectors.toList());

        List<ApartmentDto> apartmentsShortRent = allApartments.stream()
                .filter(a -> "rent".equalsIgnoreCase(a.getDealType()) && "Посуточно".equalsIgnoreCase(a.getTypeOfRent()) && a.getStatus() != null && a.getStatus().name().equals("PUBLISHED"))
                .collect(Collectors.toList());

        List<ApartmentDto> apartmentsLongRent = allApartments.stream()
                .filter(a -> "rent".equalsIgnoreCase(a.getDealType()) && "Долгосрочно".equalsIgnoreCase(a.getTypeOfRent()) && a.getStatus() != null && a.getStatus().name().equals("PUBLISHED"))
                .collect(Collectors.toList());

        model.addAttribute("apartmentsSale", apartmentsSale);
        model.addAttribute("apartmentsShortRent", apartmentsShortRent);
        model.addAttribute("apartmentsLongRent", apartmentsLongRent);

        return "apartments";
    }



    @PostMapping("/apartments")
    public String filterApartments(@ModelAttribute ApartmentFilterDto filter, Model model) {
        System.out.println("FILTER: " + filter);
        model.addAttribute("filter", filter);

        List<ApartmentDto> apartments = apartmentService.findApartmentsByFilter(filter);
        System.out.println("APARTMENTS (all): " + apartments.size());

        if ("sale".equalsIgnoreCase(filter.getDealType())) {
            model.addAttribute("apartmentsSale", apartments);
            model.addAttribute("apartmentsShortRent", List.of());
            model.addAttribute("apartmentsLongRent", List.of());
            System.out.println("APARTMENTS (sale): " + apartments.size());
        } else if ("rent".equalsIgnoreCase(filter.getDealType())) {
            if ("Посуточно".equalsIgnoreCase(filter.getRentType())) {
                model.addAttribute("apartmentsSale", List.of());
                model.addAttribute("apartmentsShortRent", apartments);
                model.addAttribute("apartmentsLongRent", List.of());
                System.out.println("APARTMENTS (short rent): " + apartments.size());
            } else if ("Долгосрочно".equalsIgnoreCase(filter.getRentType())) {
                model.addAttribute("apartmentsSale", List.of());
                model.addAttribute("apartmentsShortRent", List.of());
                model.addAttribute("apartmentsLongRent", apartments);
                System.out.println("APARTMENTS (long rent): " + apartments.size());
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