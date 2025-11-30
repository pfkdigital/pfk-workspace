package com.example.pfkworkspace.controller;

import com.example.pfkworkspace.dto.request.AuthenticationRequestDto;
import com.example.pfkworkspace.dto.request.RegisterRequestDto;
import com.example.pfkworkspace.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;

  @PostMapping("/register")
  private ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
    return new ResponseEntity<>(authService.register(registerRequestDto), HttpStatus.CREATED);
  }

  @PostMapping("/authenticate")
  private ResponseEntity<?> authenticate(
      @Valid @RequestBody AuthenticationRequestDto authenticationRequestDto,
      HttpServletResponse response) {
    return new ResponseEntity<>(
        authService.authenticate(authenticationRequestDto, response), HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
    return new ResponseEntity<>(authService.refresh(request, response), HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    return new ResponseEntity<>(authService.logout(request, response), HttpStatus.OK);
  }
}
