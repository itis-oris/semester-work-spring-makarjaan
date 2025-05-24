package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import org.springframework.web.bind.annotation.GetMapping;

public class IndexController {

    @Loggable
    @GetMapping(value = "index")
    public String index() {
        return "index";
    }
}
