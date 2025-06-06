package com.makarova.controllers;

import com.makarova.dto.ApartmentDto;
import com.makarova.dto.ProfileResponse;
import com.makarova.dto.UserDto;
import com.makarova.service.ApartmentService;
import com.makarova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final ApartmentService apartmentService;

    @GetMapping("/current")
    public ResponseEntity<ProfileResponse> getCurrentUserProfile(Principal principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        String email = "hui@ru";
        UserDto user = userService.findByEmail(email);

        List<ApartmentDto> apartmentsSale = apartmentService.getByUserIdAndDealType(user.getId(), "sale");
        List<ApartmentDto> apartmentsRent = apartmentService.getByUserIdAndDealType(user.getId(), "rent");

        ProfileResponse response = ProfileResponse.builder()
                .user(user)
                .apartmentsSale(apartmentsSale)
                .apartmentsRent(apartmentsRent)
                .build();

        return ResponseEntity.ok(response);
    }
}