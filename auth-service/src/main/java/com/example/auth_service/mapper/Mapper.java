package com.example.auth_service.mapper;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.LoginResponseDto;
import com.example.auth_service.dto.UserRegistrationResponseDto;
import com.example.auth_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UserRegistrationResponseDto mapToUserRegistrationResponseDto(User user){
        UserRegistrationResponseDto responseDto=new UserRegistrationResponseDto();

        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        responseDto.setMessage("User information saved successfully");

        return responseDto;
    }

    public LoginResponseDto mapToLoginResponseDto(User user,String token){

        LoginResponseDto loginResponseDto=new LoginResponseDto();

        loginResponseDto.setName(user.getName());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setToken(token);
        loginResponseDto.setRole(user.getRole());
        loginResponseDto.setMessage("Login Successful");

        return loginResponseDto;
    }


}
