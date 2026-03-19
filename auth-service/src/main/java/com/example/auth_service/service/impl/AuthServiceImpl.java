package com.example.auth_service.service.impl;

import com.example.auth_service.dto.*;
import com.example.auth_service.entity.User;
import com.example.auth_service.exception.EmailAlreadyExistsException;
import com.example.auth_service.exception.InvalidPasswordException;
import com.example.auth_service.exception.UserNotFoundException;
import com.example.auth_service.exception.UsernameAlreadyExistsException;
import com.example.auth_service.mapper.Mapper;
import com.example.auth_service.repository.UserRepo;
import com.example.auth_service.repository.UserServiceClient;
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

    @Autowired
    UserServiceClient userServiceClient;

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

        //Storing user to user-service-db by using OpenFein
        UserRequest userRequest=new UserRequest(user.getId(), user.getName(), user.getUsername(), user.getEmail());
        userServiceClient.createUser(userRequest);

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

    public void updateUserFromUserService(Integer id, UserUpdateRequestDto requestDto){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));

        if (requestDto.getEmail() != null &&
                !requestDto.getEmail().equals(user.getEmail()) &&
                userRepo.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (requestDto.getUsername() != null &&
                !requestDto.getUsername().equals(user.getUsername()) &&
                userRepo.existsByUsername(requestDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if(requestDto.getEmail()!=null){
            user.setEmail(requestDto.getEmail());
        }
        if(requestDto.getUsername()!=null){
            user.setUsername(requestDto.getUsername());
        }

        if(requestDto.getName()!=null){
            user.setName(requestDto.getName());
        }

        userRepo.save(user);
    }

    public void deleteUserFromUserService(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));
        userRepo.delete(user);
    }

}
