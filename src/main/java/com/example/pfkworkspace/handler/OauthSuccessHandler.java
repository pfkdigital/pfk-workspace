package com.example.pfkworkspace.handler;

import com.example.pfkworkspace.entity.User;
import com.example.pfkworkspace.exception.UserNotFoundException;
import com.example.pfkworkspace.repository.UserRepository;
import com.example.pfkworkspace.security.JwtUtility;
import com.example.pfkworkspace.utility.CookieUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtility jwtUtility;
    private final CookieUtility cookieUtility;
    private final UserRepository userRepository;

    @Value("${client.oauth.loginSuccessUrl}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String provider = oauthToken.getAuthorizedClientRegistrationId();
        String providerId = extractProviderId(oAuth2User, provider);

        if (providerId == null) {
            log.error("Provider ID was not found in successful flow");
            throw new OAuth2AuthenticationException("Provider ID is null, unable to authenticate user");
        }

    User user =
        userRepository
            .findUserByProviderId(providerId)
            .orElseThrow(() -> new UserNotFoundException("User was not found"));

        String accessToken = jwtUtility.generateAccessToken(user);
        String refreshToken = jwtUtility.generateRefreshToken(user);
        cookieUtility.addCookies(accessToken, refreshToken, response);

        log.info("User {} successfully logged in with {}", user.getUsername(), provider);
        log.info( response.getHeader(HttpHeaders.SET_COOKIE));
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String extractProviderId(OAuth2User oAuth2User, String provider) {
        return switch (provider) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id");
            default -> "unknown";
        };
    }
}
