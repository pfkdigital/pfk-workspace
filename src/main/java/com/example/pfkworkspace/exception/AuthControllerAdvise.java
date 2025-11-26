package com.example.pfkworkspace.exception;

import com.example.pfkworkspace.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AuthControllerAdvise {

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleUsernameAlreadyExistsException() {
    ApiError apiError =
        ApiError.builder()
            .message("Username already exists")
            .status(HttpStatus.CONFLICT)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(EmailAddressAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleEmailAddressAlreadyExistsException() {
    ApiError apiError =
        ApiError.builder()
            .message("Email address already exists")
            .status(HttpStatus.CONFLICT)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiError> handleUserNotFoundException() {
    ApiError apiError =
        ApiError.builder()
            .message("User not found")
            .status(HttpStatus.NOT_FOUND)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiError> handleUsernameNotFoundException() {
    ApiError apiError =
        ApiError.builder()
            .message("User not found")
            .status(HttpStatus.NOT_FOUND)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IncorrectCredentialsException.class)
  public ResponseEntity<ApiError> handleIncorrectCredentialsException() {
    ApiError apiError =
        ApiError.builder()
            .message("Incorrect credentials")
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RefreshTokenMissingException.class)
  public ResponseEntity<ApiError> handleRefreshTokenMissingException() {
    ApiError apiError =
        ApiError.builder()
            .message("Refresh token is missing")
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RefreshTokenIsNotValidException.class)
  public ResponseEntity<ApiError> handleRefreshTokenIsNotValidException() {
    ApiError apiError =
        ApiError.builder()
            .message("Refresh token is not valid")
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }
}
