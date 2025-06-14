package com.makarova.service;

import com.makarova.dto.UserDto;
import com.makarova.entity.User;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


public interface UserService {
    void register(UserDto userDto) throws AuthException;
    UserDto findByEmail(String email);
    Optional<User> getByLogin(@NonNull String login);

    void updateProfilePhoto(UserDto user, MultipartFile profilePhoto) throws IOException;
    void deleteProfilePhoto(UserDto userDto);
    Boolean changePassword(UserDto user, String currentPassword, String newPassword);
    void deleteAccount(UserDto user);
    void updateUserInfo(UserDto userDto);
}