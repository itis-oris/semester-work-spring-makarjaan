package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import com.makarova.repository.ApartmentRepository;
import com.makarova.service.ApartmentService;
import com.makarova.service.PhotoService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/addadvert")
@RequiredArgsConstructor
public class AdvertController {

    private final UserService userService;

    @GetMapping
    public String showAddForm(
            Model model,
            Principal principal,
            @RequestParam(required = false) String dealType,
            @RequestParam(required = false) String propertyType) {

//        if (principal == null) {
//            return "redirect:/login";
//        }

        String email = "hui@ru";
        UserDto user = userService.findByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("dealType", dealType);
        model.addAttribute("propertyType", propertyType);

        return "add_advert";
    }
}