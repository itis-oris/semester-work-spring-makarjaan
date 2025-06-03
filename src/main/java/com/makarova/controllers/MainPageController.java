package com.makarova.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/main")
    public String getMainPage() {
        return "home_page";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "profile_page";
    }

}
