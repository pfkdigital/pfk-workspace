package com.example.pfkworkspace.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public class ApiError {
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;
}
