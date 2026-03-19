package com.example.user_service.repo;

import com.example.user_service.dto.UserUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    @PatchMapping("/internal/auth/users/{id}")
    void updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDto requestDto);

    @DeleteMapping("/internal/auth/users/{id}")
    void deleteUser(@PathVariable Integer id);

}
