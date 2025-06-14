package com.makarova.dto;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
}