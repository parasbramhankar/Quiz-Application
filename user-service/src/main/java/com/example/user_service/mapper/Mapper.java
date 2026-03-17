package com.example.user_service.mapper;

import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;

public class Mapper {

    public UserResponseDto mapToResponseDto(User user){
        UserResponseDto userResponseDto=new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setBio(user.getBio());
        userResponseDto.setProfilePic(user.getProfilePic());
        userResponseDto.setPoints(user.getPoints());
        userResponseDto.setAuthId(user.getAuthId());

        return  userResponseDto;
    }
}
