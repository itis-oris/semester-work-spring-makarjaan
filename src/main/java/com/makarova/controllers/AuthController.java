package com.makarova.controllers;

import com.makarova.dto.JwtRefreshRequest;
import com.makarova.dto.JwtResponse;
import com.makarova.dto.JwtRequest;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRefreshRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtRefreshRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getToken());
        return ResponseEntity.ok(token);
    }

}