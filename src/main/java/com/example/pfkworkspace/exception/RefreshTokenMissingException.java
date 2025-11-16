package com.example.pfkworkspace.exception;

public class RefreshTokenMissingException extends RuntimeException {
  public RefreshTokenMissingException(String message) {
    super(message);
  }
}
