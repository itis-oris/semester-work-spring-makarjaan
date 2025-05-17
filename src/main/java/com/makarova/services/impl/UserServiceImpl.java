package com.makarova.services.impl;

import com.makarova.dto.UserDto;
import com.makarova.models.User;
import com.makarova.repositories.UsersRepository;
import com.makarova.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDto userDto) {
        User newUser = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        usersRepository.save(newUser);
    }
}
