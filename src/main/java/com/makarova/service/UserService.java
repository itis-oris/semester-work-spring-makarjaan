package com.makarova.service;

import com.makarova.dto.JwtResponse;
import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;

import java.util.Optional;


public interface UserService {
    JwtResponse register(UserDto userDto) throws AuthException;
    void authenticate(String email, String password) throws AuthException;
    Optional<User> getByLogin(@NonNull String login);
}