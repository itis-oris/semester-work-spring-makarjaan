package com.makarova.service.impl;

import com.makarova.dto.UserDto;
import com.makarova.entity.Role;
import com.makarova.entity.User;
import com.makarova.repository.RefreshTokenRepository;
import com.makarova.repository.UserRepository;
import com.makarova.service.ApartmentService;
import com.makarova.service.UserService;
import com.makarova.utils.CloudinaryUtil;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CloudinaryUtil cloudinaryUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ApartmentService apartmentService;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, CloudinaryUtil cloudinaryUtil, ApartmentService apartmentService, RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cloudinaryUtil = cloudinaryUtil;
        this.apartmentService = apartmentService;
        this.refreshTokenRepository = refreshTokenRepository;
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
                .createdAt(LocalDateTime.now())
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


    @Override
    @Transactional
    public void updateProfilePhoto(UserDto user, MultipartFile profilePhoto) throws IOException {
        File tempFile = File.createTempFile("upload-", ".tmp");
        try {
            profilePhoto.transferTo(tempFile);
            String photoUrl = cloudinaryUtil.upload(tempFile, "photoUrl/" + user.getId());
            userRepository.updateProfilePhotoUrl(user.getId(), photoUrl);
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
    }

    @Override
    public void deleteProfilePhoto(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePhotoUrl(null);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public Boolean changePassword(UserDto user, String currentPassword, String newPassword) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(currentPassword, existingUser.getHashPassword())) {
            return false;
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Пароль должен быть не менее 8 символов");
        }

        existingUser.setHashPassword(passwordEncoder.encode(newPassword));

        return true;
    }

    @Override
    @Transactional
    public void deleteAccount(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        refreshTokenRepository.deleteByUserId(user.getId());
        apartmentService.deleteByUser(userDto);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getMessengers() != null) {
            user.setMessengers(userDto.getMessengers());
        }

        userRepository.save(user);
    }


}
