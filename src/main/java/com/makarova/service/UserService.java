package com.makarova.service;

import com.makarova.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    void registerUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDetailsService userDetailsService();
}