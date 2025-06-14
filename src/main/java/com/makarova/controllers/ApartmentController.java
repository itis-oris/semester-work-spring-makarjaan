package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.ApartmentFilterDto;
import com.makarova.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public String showApartments(@ModelAttribute ApartmentFilterDto filter, Model model) {
        List<ApartmentDto> apartments = apartmentService.findApartmentsByFilter(filter);
        model.addAttribute("apartments", apartments);
        model.addAttribute("filter", filter);
        return "apartments";
    }
} 