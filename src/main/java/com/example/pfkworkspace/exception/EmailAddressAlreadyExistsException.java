package com.example.pfkworkspace.exception;

public class EmailAddressAlreadyExistsException extends RuntimeException {
    public EmailAddressAlreadyExistsException(String message) {
        super(message);
    }
}
