package com.makarova.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdvertController {

    @GetMapping("/addadvert")
    public String showAddForm() {
        return "add_advert";
    }
}