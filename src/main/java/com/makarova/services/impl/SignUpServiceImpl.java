package com.makarova.services.impl;

import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import com.makarova.repositories.UsersRepository;
import com.makarova.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDto userDto) {

        if (usersRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User newUser = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phone(userDto.getPhone())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .role(User.Role.USER)
                .state(User.State.ACTIVE)
                .build();

        usersRepository.save(newUser);
    }
}
