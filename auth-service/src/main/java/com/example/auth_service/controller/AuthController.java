package com.example.auth_service.controller;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.LoginResponseDto;
import com.example.auth_service.dto.UserRegistrationRequestDto;
import com.example.auth_service.dto.UserRegistrationResponseDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto>registerUser(@RequestBody UserRegistrationRequestDto requestDto){
        UserRegistrationResponseDto responseDto=authService.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>loginUser(@RequestBody LoginRequestDto requestDto){
        LoginResponseDto responseDto=authService.loginUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String header){

        String token = header.substring(7);
        return ResponseEntity.ok(authService.validateToken(token));
    }

    
}
