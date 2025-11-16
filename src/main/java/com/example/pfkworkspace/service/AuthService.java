package com.example.pfkworkspace.service;

import com.example.pfkworkspace.dto.request.AuthenticationRequestDto;
import com.example.pfkworkspace.dto.request.RegisterRequestDto;
import com.example.pfkworkspace.dto.response.AuthenticationResponseDto;
import com.example.pfkworkspace.dto.response.RefreshResponseDto;
import com.example.pfkworkspace.dto.response.RegistrationResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    RegistrationResponseDto register(RegisterRequestDto registerRequestDto);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto, HttpServletResponse response);
    RefreshResponseDto refresh(HttpServletRequest request, HttpServletResponse response);
}
