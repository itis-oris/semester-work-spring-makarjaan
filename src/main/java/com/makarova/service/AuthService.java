package com.makarova.service;

import com.makarova.dto.JwtResponse;
import com.makarova.dto.LoginRequest;
import jakarta.security.auth.message.AuthException;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse token(String token) throws AuthException;
    JwtResponse refresh(String token) throws AuthException;
}