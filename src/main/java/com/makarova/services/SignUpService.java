package com.makarova.services;

import com.makarova.dto.UserDto;

public interface SignUpService {
    void registerUser(UserDto userDto);
}