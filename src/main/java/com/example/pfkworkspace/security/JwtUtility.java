package com.example.pfkworkspace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JwtUtility {

  @Value("${jwt.secret.key}")
  private String secretKey;

  @Value("${jwt.expiration.access-token}")
  private Long accessTokenExpirationMs;

  @Value("${jwt.expiration.refresh-token}")
  private Long refreshTokenExpirationMs;

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration();
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String generateAccessToken(UserDetails userDetails) {
    return buildToken(userDetails, accessTokenExpirationMs);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(userDetails, refreshTokenExpirationMs);
  }

  private String buildToken(UserDetails userDetails, Long expirationMs) {

    Map<String, Object> claims =
        Map.of(
            "roles",
            userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

    return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSecretKey())
        .compact();
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
  }
}
