package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import com.makarova.dto.JwtResponse;
import com.makarova.dto.LoginRequest;
import com.makarova.dto.RegisterRequest;
import com.makarova.dto.UserDto;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class UserController {

    @Autowired
    private UserService userService;



    @Loggable
    @GetMapping("/signUp")
    public String getRegistrationPage() {
        return "sign_up_page";
    }

    @PostMapping("/signUp")
    public String handleRegistration(
            @Valid @ModelAttribute("user") RegisterRequest user,
            BindingResult result,
            Model model,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "sign_up_page";
        }

        try {
            userService.register(user);
            return "redirect:/main";

        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "sign_up_page";
        }
    }

}