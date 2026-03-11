package com.example.auth_service.service.impl;

import com.example.auth_service.dto.LoginRequestDto;
import com.example.auth_service.dto.LoginResponseDto;
import com.example.auth_service.dto.UserRegistrationRequestDto;
import com.example.auth_service.dto.UserRegistrationResponseDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.exception.EmailAlreadyExistsException;
import com.example.auth_service.exception.InvalidPasswordException;
import com.example.auth_service.exception.UserNotFoundException;
import com.example.auth_service.exception.UsernameAlreadyExistsException;
import com.example.auth_service.mapper.Mapper;
import com.example.auth_service.repository.UserRepo;
import com.example.auth_service.security.JwtService;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Mapper mapper;

    @Autowired
    JwtService jwtService;

    @Override
    public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto) {
        if(userRepo.existsByEmail(requestDto.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if(userRepo.existsByUsername(requestDto.getUsername())){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user=new User();
        user.setName(requestDto.getName());
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User saveUser=userRepo.save(user);
        return mapper.mapToUserRegistrationResponseDto(saveUser);
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto requestDto) {
        User user=userRepo.findByEmailOrUsername(requestDto.getIdentifier(), requestDto.getIdentifier())
                .orElseThrow(()->new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new InvalidPasswordException("Invalid username or password");
        }

        String token=jwtService.generateToken(user);

        return mapper.mapToLoginResponseDto(user,token);
    }

    @Override
    public boolean validateToken(String token) {
        try{
            return jwtService.isValid(token);
        }catch (Exception e){
            return false;
        }
    }

}
