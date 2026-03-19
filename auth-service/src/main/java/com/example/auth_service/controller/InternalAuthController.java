package com.example.auth_service.controller;

import com.example.auth_service.dto.UserUpdateRequestDto;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auth")
public class InternalAuthController {

    @Autowired
    AuthService authService;

    @PatchMapping("/users/{id}")
    void updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDto requestDto){
        authService.updateUserFromUserService(id,requestDto);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Integer id){
        authService.deleteUserFromUserService(id);
    }
}
