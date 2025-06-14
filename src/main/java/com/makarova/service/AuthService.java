package com.makarova.service;

import com.makarova.dto.JwtRequest;
import com.makarova.dto.JwtResponse;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;

public interface AuthService {
    JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException;
    JwtResponse getAccessToken(String token) throws AuthException;
    JwtResponse refresh(String token) throws AuthException;
    void logout(String refreshToken);
}