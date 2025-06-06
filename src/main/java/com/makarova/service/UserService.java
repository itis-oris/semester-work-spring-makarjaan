package com.makarova.service;

import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;

import java.util.Optional;


public interface UserService {
    void register(UserDto userDto) throws AuthException;
    Optional<User> getByLogin(@NonNull String login);
}