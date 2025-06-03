package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import com.makarova.dto.JwtRequest;
import com.makarova.dto.JwtResponse;
import com.makarova.dto.UserDto;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Loggable
    @GetMapping("/signUp")
    public String getRegistrationPage() {
        return "sign_up_page";
    }

    @PostMapping("/signUp")
    public String handleRegistration(
            @Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "sign_up_page";
        }

        try {
            userService.register(user);
            return "redirect:/signIn";

        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "sign_up_page";
        }
    }


    @GetMapping("/signIn")
    public String getLoginPage() {
        return "sign_in_page";
    }
}
