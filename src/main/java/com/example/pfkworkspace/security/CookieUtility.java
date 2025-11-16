package com.example.pfkworkspace.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtility {
  @Value("${jwt.expiration.access-token}")
  private Long accessTokenExpirationMs;

  @Value("${jwt.expiration.refresh-token}")
  private Long refreshTokenExpirationMs;

  public ResponseCookie createAccessTokenCookie(String token) {
    return ResponseCookie.from("accessToken", token)
        .httpOnly(true)
        .secure(true)
        .maxAge(accessTokenExpirationMs / 1000)
        .path("/")
        .sameSite("None")
        .build();
  }

  public ResponseCookie createRefreshTokenCookie(String token) {
    return ResponseCookie.from("refresh_token", token)
        .httpOnly(true)
        .secure(true)
        .maxAge(refreshTokenExpirationMs / 1000)
        .path("/")
        .sameSite("None")
        .build();
  }
}
