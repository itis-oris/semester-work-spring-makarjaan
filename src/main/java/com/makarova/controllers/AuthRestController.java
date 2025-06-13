package com.makarova.controllers;

import com.makarova.dto.JwtRequest;
import com.makarova.dto.JwtResponse;
import com.makarova.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest authRequest,
            HttpServletResponse response) throws AuthException {

        final JwtResponse tokens = authService.login(authRequest);

        Cookie refreshCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(refreshCookie);
        Cookie accessCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60);
        response.addCookie(accessCookie);

        System.out.println("Login successful for: " + authRequest.getEmail());
        System.out.println("Refresh token set in cookie");

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) throws AuthException {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AuthException("Refresh token missing");
        }

        final JwtResponse token = authService.getAccessToken(refreshToken);
        System.out.println("New access token generated for refresh token");
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(
            @CookieValue(name = "refreshToken") String refreshToken) throws AuthException {

        final JwtResponse token = authService.refresh(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {

        authService.logout(refreshToken);

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/signIn");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")
    public ResponseEntity<Void> verifyToken() {
        return ResponseEntity.ok().build();
    }

}