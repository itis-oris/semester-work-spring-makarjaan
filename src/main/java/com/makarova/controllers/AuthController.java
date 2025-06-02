package com.makarova.controllers;

import com.makarova.dto.JwtRefreshRequest;
import com.makarova.dto.JwtResponse;
import com.makarova.dto.LoginRequest;
import com.makarova.dto.RegisterRequest;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req, HttpServletResponse response) {
        JwtResponse jwt = userService.register(req);
        setCookies(response, jwt);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        JwtResponse jwt = authService.login(req);
        setCookies(response, jwt);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody JwtRefreshRequest req, HttpServletResponse response) throws Exception {
        JwtResponse jwt = authService.refresh(req.getToken());
        setCookies(response, jwt);
        return ResponseEntity.ok(jwt);
    }

    private void setCookies(HttpServletResponse response, JwtResponse jwt) {
        Cookie accessToken = new Cookie("access_token", jwt.getAccessToken());
        accessToken.setHttpOnly(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(60 * 60);

        Cookie refreshToken = new Cookie("refresh_token", jwt.getRefreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }
}