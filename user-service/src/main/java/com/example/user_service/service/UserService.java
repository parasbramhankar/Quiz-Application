package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.entity.User;
import com.example.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    UserRepo userRepo;

    public User createUser(UserRequestDto userRequest) {
        User user = new User();
        user.setAuthId(userRequest.getAuthId());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(user.getUsername());

        return userRepo.save(user);
    }


}



