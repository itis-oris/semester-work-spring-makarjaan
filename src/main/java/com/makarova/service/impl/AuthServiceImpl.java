package com.makarova.service.impl;

import com.makarova.dto.JwtResponse;
import com.makarova.dto.LoginRequest;
import com.makarova.entity.User;
import com.makarova.filter.JwtProvider;
import com.makarova.repository.UserRepository;
import com.makarova.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()));
        if (passwordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        }
        return null;
    }

    @Override
    public JwtResponse token(String token) throws AuthException {
        if (jwtProvider.validateRefresh(token)) {
            Claims claims = jwtProvider.getRefreshClaims(token);
            String email = claims.getSubject();
            String savedRefresh = refreshStorage.get(email);
            if (savedRefresh != null && savedRefresh.equals(token)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new AuthException(email));
                String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        throw new AuthException("Invalid refresh token");
    }

    @Override
    public JwtResponse refresh(String token) throws AuthException {
        if (jwtProvider.validateRefresh(token)) {
            Claims claims = jwtProvider.getRefreshClaims(token);
            String email = claims.getSubject();
            String savedRefresh = refreshStorage.get(email);
            if (savedRefresh != null && savedRefresh.equals(token)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new AuthException(email));
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(email, newRefreshToken);
                return new JwtResponse(accessToken, null);
            }
        }
        throw new AuthException("Invalid refresh token");
    }
}