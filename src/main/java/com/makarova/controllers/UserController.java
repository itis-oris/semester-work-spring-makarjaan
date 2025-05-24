package com.makarova.controllers;

import com.makarova.aspect.Loggable;
import com.makarova.dto.UserDto;
import com.makarova.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
class UserController {

    @Autowired
    private UserService registrationService;

    @Loggable
    @GetMapping("/registration")
    public String getRegistrationPage() throws Exception {
        return "registration";
    }

}