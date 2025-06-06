package com.makarova.service.impl;

import com.makarova.dto.JwtRequest;
import com.makarova.dto.JwtResponse;
import com.makarova.entity.RefreshToken;
import com.makarova.entity.User;
import com.makarova.filter.JwtAuthentication;
import com.makarova.filter.JwtProvider;
import com.makarova.repository.RefreshTokenRepository;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public JwtResponse login(JwtRequest request) throws AuthException {
        User user = userService.getByLogin(request.getEmail())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            throw new AuthException("Неправильный пароль");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse getAccessToken(String refreshToken) throws AuthException {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException("Invalid refresh token");
        }

        // Проверяем наличие токена в базе
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException("Refresh token not found in storage"));

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();

        User user = userService.getByLogin(login)
                .orElseThrow(() -> new AuthException("User not found"));

        // Проверяем соответствие пользователя
        if (!storedToken.getUser().getId().equals(user.getId())) {
            throw new AuthException("Token user mismatch");
        }

        // Генерируем новый access token
        String newAccessToken = jwtProvider.generateAccessToken(user);

        System.out.println("Generated new access token for: " + login);
        return new JwtResponse(newAccessToken, null);
    }

    @Transactional
    @Override
    public JwtResponse refresh(String refreshToken) throws AuthException {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException("Invalid refresh token");
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String login = claims.getSubject();

        // Проверяем наличие токена в базе
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException("Refresh token not found in storage"));

        User user = userService.getByLogin(login)
                .orElseThrow(() -> new AuthException("User not found"));

        // Проверяем соответствие пользователя
        if (!storedToken.getUser().getId().equals(user.getId())) {
            throw new AuthException("Token user mismatch");
        }

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        // Обновляем токен в базе
        refreshTokenRepository.delete(storedToken);
        saveRefreshToken(user, newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    private void saveRefreshToken(User user, String token) {
        // Удаляем старые токены пользователя перед сохранением нового
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtProvider.getRefreshExpiration()));
        refreshTokenRepository.save(refreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

}