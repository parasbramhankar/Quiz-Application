package com.example.auth_service.service;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.LoginResponseDto;
import com.example.auth_service.dto.UserRegistrationRequestDto;
import com.example.auth_service.dto.UserRegistrationResponseDto;
import org.springframework.stereotype.Service;


public interface AuthService {
    UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto);

    LoginResponseDto loginUser(LoginRequestDto requestDto);

    boolean validateToken(String token);
}


