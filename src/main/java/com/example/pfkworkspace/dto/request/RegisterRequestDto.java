package com.example.pfkworkspace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
  @NotBlank(message = "username is required")
  @Size(min = 3, max = 30, message = "username must be between 3 and 30 characters")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "username contains invalid characters")
  private String username;

  @NotBlank(message = "password is required")
  @Size(min = 8, max = 128, message = "password must be between 8 and 128 characters")
  private String password;

  @NotBlank(message = "email is required")
  @Email(message = "email must be a valid email address")
  private String emailAddress;

  @Size(max = 50, message = "first name must be at most 50 characters")
  private String firstName;

  @Size(max = 50, message = "last name must be at most 50 characters")
  private String lastName;
}
