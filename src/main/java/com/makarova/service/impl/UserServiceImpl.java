package com.makarova.service.impl;

import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import com.makarova.repository.UsersRepository;
import com.makarova.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phone(userDto.getPhone())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .role(User.Role.USER)
                .state(User.State.ACTIVE)
                .build();

        userRepository.save(user);
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .phone(user.getPhone())
                        .build())
                .collect(Collectors.toList());
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }


    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(email);
    }

    @Deprecated
    public void grantAdminRole() {
        User user = getCurrentUser();
        user.setRole(User.Role.ADMIN);
        userRepository.save(user);
    }
}
