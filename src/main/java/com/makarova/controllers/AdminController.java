package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.makarova.entity.ApartmentStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ApartmentService apartmentService;
    private final PhotoService photoService;

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<ApartmentDto> apartmentsRent = apartmentService.findApartmentsForRentAdmin();
        List<ApartmentDto> apartmentsSale = apartmentService.findApartmentsForSaleAdmin();

        for (ApartmentDto apartment : apartmentsRent) {
            String mainPhotoUrl = photoService.getMainPhoto(apartment.getId(), "rent");
            apartment.setMainPhotoUrl(mainPhotoUrl);
        }

        for (ApartmentDto apartment : apartmentsSale) {
            String mainPhotoUrl = photoService.getMainPhoto(apartment.getId(), "sale");
            apartment.setMainPhotoUrl(mainPhotoUrl);
        }

        model.addAttribute("apartmentsRentAdmin", apartmentsRent);
        model.addAttribute("apartmentsSaleAdmin", apartmentsSale);

        return "admin";
    }

    @PostMapping("/admin")
    public String handleAdminAction(@RequestParam("apartmentId") Long apartmentId,
                                    @RequestParam("dealType") String dealType,
                                    @RequestParam("action") String action) {
        if ("approve".equals(action)) {
            apartmentService.changeStatus(apartmentId, ApartmentStatus.PUBLISHED);
        } else if ("reject".equals(action)) {
            apartmentService.changeStatus(apartmentId, ApartmentStatus.REFUSED);
        }
        return "redirect:/admin";
    }
}