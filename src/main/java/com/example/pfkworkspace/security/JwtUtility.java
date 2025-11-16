package com.example.pfkworkspace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Configuration
public class JwtUtility {
    @Value("${security.jwt.secret.key}")
    private String secretKey;

    @Value("${security.jwt.expiration.ms}")
    private Long accessTokenExpirationMs;

    @Value("${security.jwt.refresh.expiration.ms}")
    private Long refreshTokenExpirationMs;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).after(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpirationMs, Map.of());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpirationMs, Map.of());
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser().decryptWith(getSecretKey()).build().parse(token).getPayload();
    }

    private String buildToken(UserDetails userDetails, Long expirationMs, Map<String, Object> claims) {
        return Jwts.builder().claims(claims).claim("authorities", userDetails.getAuthorities())
                .subject(userDetails.getUsername())
                .signWith(getSecretKey())
                .expiration(new java.util.Date(System.currentTimeMillis() + expirationMs))
                .compact();
    }
}
