package com.example.pfkworkspace.utility;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtility {
  @Value("${jwt.expiration.access-token}")
  private Long accessTokenExpirationMs;

  @Value("${jwt.expiration.refresh-token}")
  private Long refreshTokenExpirationMs;

  public ResponseCookie createAccessTokenCookie(String token) {
    return ResponseCookie.from("access_token", token)
        .httpOnly(true)
        .secure(true)
        .sameSite("None")
        .path("/")
        .maxAge(accessTokenExpirationMs / 1000)
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

  public void addCookies(String accessToken, String refreshToken, HttpServletResponse response) {
    ResponseCookie accessTokenCookie = createAccessTokenCookie(accessToken);
    ResponseCookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);

    response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
  }

    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access_token") || cookie.getName().equals("refresh_token")) {

                cookie.setValue("");
                cookie.setMaxAge(0);
                cookie.setPath("/");

                response.addCookie(cookie);
            }
        }
    }

}
