package com.example.pfkworkspace.service.impl;

import com.example.pfkworkspace.dto.request.AuthenticationRequestDto;
import com.example.pfkworkspace.dto.request.RegisterRequestDto;
import com.example.pfkworkspace.dto.response.AuthenticationResponseDto;
import com.example.pfkworkspace.dto.response.RefreshResponseDto;
import com.example.pfkworkspace.dto.response.RegistrationResponseDto;
import com.example.pfkworkspace.entity.User;
import com.example.pfkworkspace.enums.Roles;
import com.example.pfkworkspace.exception.*;
import com.example.pfkworkspace.security.CookieUtility;
import com.example.pfkworkspace.security.JwtUtility;
import com.example.pfkworkspace.service.AuthService;
import com.example.pfkworkspace.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final CookieUtility cookieUtility;
  private final JwtUtility jwtUtility;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public RegistrationResponseDto register(RegisterRequestDto registerRequestDto) {
    if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
      log.info("Username already exists");
      throw new UsernameAlreadyExistsException("Username already exists");
    }

    if (userRepository.existsByEmailAddress(registerRequestDto.getEmailAddress())) {
      log.info("Email address already exists");
      throw new EmailAddressAlreadyExistsException("Email address already exists");
    }

    User newUser =
        User.builder()
            .firstName(registerRequestDto.getFirstName())
            .lastName(registerRequestDto.getLastName())
            .username(registerRequestDto.getUsername())
            .passwordHash(passwordEncoder.encode(registerRequestDto.getPassword()))
            .emailAddress(registerRequestDto.getEmailAddress())
            .role(Roles.ROLE_USER)
            .isEnabled(true)
            .build();
    userRepository.save(newUser);

    log.info("User registered successfully");
    return RegistrationResponseDto.builder()
        .message("User registered successfully, please log in")
        .build();
  }

  @Override
  public AuthenticationResponseDto authenticate(
      AuthenticationRequestDto authenticationRequestDto, HttpServletResponse response) {
    User user = userRepository.findUserByUsername(authenticationRequestDto.getUsername());

    if (user == null) {
      log.info("User not found");
      throw new UserNotFoundException("User not found");
    }

    if (!passwordEncoder.matches(authenticationRequestDto.getPassword(), user.getPasswordHash())) {
      log.info("Incorrect credentials");
      throw new IncorrectCredentialsException(
          "Incorrect credentials, are you sure you have the correct username and password?");
    }

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPasswordHash());
    authenticationManager.authenticate(token);

    String accessToken = jwtUtility.generateAccessToken(user);
    String refreshToken = jwtUtility.generateRefreshToken(user);

    ResponseCookie accessTokenCookie = cookieUtility.createAccessTokenCookie(accessToken);
    ResponseCookie refreshTokenCookie = cookieUtility.createRefreshTokenCookie(refreshToken);

    response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    log.info("Login was successful");
    return AuthenticationResponseDto.builder().message("Login was successful").build();
  }

  @Override
  public RefreshResponseDto refresh(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = request.getHeader("refreshToken");
    if (refreshToken == null) {
      log.info("Refresh token is missing");
      throw new RefreshTokenMissingException("Refresh token is missing");
    }

    String username = jwtUtility.extractUsername(refreshToken);
    User user = userRepository.findUserByUsername(username);

    if (username == null
        || !userRepository.existsByUsername(username)
        || !jwtUtility.isTokenValid(refreshToken, user)) {
      throw new RefreshTokenIsNotValidException("Refresh token is not valid");
    }

    String accessToken = jwtUtility.generateAccessToken(user);

    ResponseCookie accessTokenCookie = cookieUtility.createAccessTokenCookie(accessToken);

    response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

    return RefreshResponseDto.builder().message("Refresh was successful").build();
  }
}
