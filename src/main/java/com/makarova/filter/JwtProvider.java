package com.makarova.filter;

import com.makarova.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationTime = now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .claim("name", user.getUsername())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationTime = now.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(accessExpirationTime))
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccess(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefresh(String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJwt(token);
            return true;
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (MalformedJwtException e) {
        } catch (SignatureException e) {
        } catch (Exception e) {
        }
        return false;
    }
}