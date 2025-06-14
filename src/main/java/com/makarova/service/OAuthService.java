//package com.makarova.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.makarova.dto.GoogleUserInfo;
//import com.makarova.dto.OAuthTokenResponse;
//import com.makarova.entity.User;
//import com.makarova.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.*;
//import org.springframework.http.converter.FormHttpMessageConverter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class OAuthService {
//
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${oauth.google.client-id}")
//    private String clientId;
//
//    @Value("${oauth.google.client-secret}")
//    private String clientSecret;
//
//    @Value("${oauth.google.redirect-uri}")
//    private String redirectUri;
//
//    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
//    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
//    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
//
//    public String getGoogleAuthUrl() {
//        return UriComponentsBuilder.fromHttpUrl(GOOGLE_AUTH_URL)
//                .queryParam("client_id", clientId)
//                .queryParam("redirect_uri", redirectUri)
//                .queryParam("response_type", "code")
//                .queryParam("scope", "email profile")
//                .queryParam("access_type", "offline")
//                .queryParam("prompt", "consent")
//                .build()
//                .toUriString();
//    }
//
//    public OAuthTokenResponse getGoogleToken(String code) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("code", code);
//        body.add("grant_type", "authorization_code");
//        body.add("redirect_uri", redirectUri);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<OAuthTokenResponse> response = restTemplate.postForEntity(
//                    GOOGLE_TOKEN_URL,
//                    request,
//                    OAuthTokenResponse.class
//            );
//            return response.getBody();
//        } catch (Exception e) {
//            log.error("Error getting token", e);
//            throw new RuntimeException("Failed to get Google token: " + e.getMessage(), e);
//        }
//    }
//
//    public GoogleUserInfo getGoogleUserInfo(String accessToken) {
//        log.info("Getting user info with token: {}", accessToken);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//
//        HttpEntity<?> request = new HttpEntity<>(headers);
//        try {
//            ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
//                    GOOGLE_USER_INFO_URL,
//                    HttpMethod.GET,
//                    request,
//                    GoogleUserInfo.class
//            );
//            log.info("User info response: {}", response.getBody());
//            return response.getBody();
//        } catch (Exception e) {
//            log.error("Error getting user info", e);
//            throw new RuntimeException("Failed to get Google user info: " + e.getMessage(), e);
//        }
//    }
//
//    public User getOrCreateParticipant(GoogleUserInfo userInfo) {
//        log.info("Getting or creating participant for user: {}", userInfo.getEmail());
//        return userRepository.findByEmail(userInfo.getEmail())
//                .orElseGet(() -> {
//                    log.info("Creating new participant for user: {}", userInfo.getEmail());
//                    User user = new User();
//                    user.setEmail(userInfo.getEmail());
//                    user.setUsername(userInfo.getName());
//                    String randomPassword = generateRandomPassword();
//                    user.setHashPassword(passwordEncoder.encode(randomPassword));
//                    log.info("Saving new user: {}", user);
//                    return userRepository.save(user);
//                });
//    }
//
//    private String generateRandomPassword() {
//        byte[] randomBytes = new byte[32];
//        new java.security.SecureRandom().nextBytes(randomBytes);
//        return Base64.getEncoder().encodeToString(randomBytes);
//    }
//}
//
