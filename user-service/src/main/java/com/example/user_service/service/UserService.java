package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import com.example.user_service.mapper.Mapper;
import com.example.user_service.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepo userRepo;

    private final Mapper mapper;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        User user = new User();
        user.setAuthId(userRequest.getAuthId());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(user.getUsername());

        User saveUser=userRepo.save(user);

        return mapper.mapToResponseDtoResponseDto(saveUser);
    }


}



