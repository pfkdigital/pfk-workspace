package com.example.pfkworkspace.exception;

import com.example.pfkworkspace.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AuthControllerAdvise {

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<ApiError> handleAuthenticationException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();
    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleUsernameAlreadyExistsException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.CONFLICT)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(EmailAddressAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleEmailAddressAlreadyExistsException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.CONFLICT)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiError> handleUserNotFoundException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.NOT_FOUND)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiError> handleUsernameNotFoundException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.NOT_FOUND)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IncorrectCredentialsException.class)
  public ResponseEntity<ApiError> handleIncorrectCredentialsException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RefreshTokenMissingException.class)
  public ResponseEntity<ApiError> handleRefreshTokenMissingException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RefreshTokenIsNotValidException.class)
  public ResponseEntity<ApiError> handleRefreshTokenIsNotValidException(Exception e) {
    ApiError apiError =
        ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
  }
}
