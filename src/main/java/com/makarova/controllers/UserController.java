package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import com.makarova.dto.UserDto;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


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
    @ResponseBody
    public void handleRegistration(
            @Valid @RequestBody UserDto userDto) throws AuthException {
        userService.register(userDto);
    }


    @GetMapping("/signIn")
    public String getLoginPage() {
        return "sign_in_page";
    }
}
