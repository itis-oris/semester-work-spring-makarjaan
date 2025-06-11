package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
class UserController {

    @Loggable
    @GetMapping("/signUp")
    public String getRegistrationPage() {
        return "sign_up_page";
    }


    @GetMapping("/signIn")
    public String getLoginPage() {
        return "sign_in_page";
    }
}
