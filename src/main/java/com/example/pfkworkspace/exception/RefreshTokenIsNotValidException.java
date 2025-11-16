package com.example.pfkworkspace.exception;

public class RefreshTokenIsNotValidException extends RuntimeException {
  public RefreshTokenIsNotValidException(String message) {
    super(message);
  }
}
