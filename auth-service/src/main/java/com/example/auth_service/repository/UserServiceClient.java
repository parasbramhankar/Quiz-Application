package com.example.auth_service.repository;


import com.example.auth_service.dto.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @PostMapping("/internal/users")
    void createUser(UserRequest userRequest);

}
