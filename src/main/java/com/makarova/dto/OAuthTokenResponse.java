package com.makarova.dto;

import lombok.Data;

@Data
public class OAuthTokenResponse {
    private String access_token;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
}

