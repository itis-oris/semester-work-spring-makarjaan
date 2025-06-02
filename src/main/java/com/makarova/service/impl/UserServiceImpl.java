package com.makarova.service.impl;

import com.makarova.dto.JwtResponse;
import com.makarova.dto.LoginRequest;
import com.makarova.dto.RegisterRequest;
import com.makarova.entity.Role;
import com.makarova.entity.User;
import com.makarova.repository.UserRepository;
import com.makarova.service.AuthService;
import com.makarova.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;


    @Override
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .phone(request.getPhone())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(Role.USER))
                .state(User.State.ACTIVE)
                .build();

        userRepository.save(newUser);

        return authService.login(new LoginRequest(request.getEmail(), request.getPassword()));
    }

}
