package com.makarova.dto;

import com.makarova.entity.Role;
import com.makarova.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.*;
import jakarta.validation.constraints.*;

import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^(\\+7|8)[0-9]{10}$", message = "Телефон должен быть в формате +7XXXXXXXXXX")
    private String phone;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).*$", message = "Пароль должен содержать минимум одну заглавную букву и одну цифру")
    private String password;

    private String profilePhotoUrl;

    private String messengers;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .messengers(user.getMessengers())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .build();
    }
}
