package com.example.auth_service.service;

import com.example.auth_service.dto.*;
import org.springframework.stereotype.Service;


public interface AuthService {
    UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto);

    LoginResponseDto loginUser(LoginRequestDto requestDto);

    boolean validateToken(String token);

    void updateUserFromUserService(Integer id, UserUpdateRequestDto requestDto);

    void deleteUserFromUserService(Integer id);
}