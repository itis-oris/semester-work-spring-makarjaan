package com.makarova.service.impl;

import com.makarova.dto.UserDto;
import com.makarova.entity.Role;
import com.makarova.entity.User;
import com.makarova.repository.UserRepository;
import com.makarova.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public void register(UserDto userDto) throws AuthException {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new AuthException("Пользователь с таким email уже существует");
        }

        User newUser = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phone(userDto.getPhone())
                .hashPassword(passwordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singleton(Role.USER))
                .state(User.State.ACTIVE)
                .build();

        userRepository.save(newUser);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::from)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }


    @Override
    public Optional<User> getByLogin(@NonNull String email) {
        return userRepository.findByEmail(email).stream().findFirst();
    }
}
