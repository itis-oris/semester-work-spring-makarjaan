package com.makarova.service;

import com.makarova.dto.JwtResponse;
import com.makarova.dto.RegisterRequest;
import com.makarova.entity.User;


public interface UserService {
    JwtResponse register(RegisterRequest request);
}