package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import com.example.user_service.exception.EmailNotFoundException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.exception.UsernameNotFoundException;
import com.example.user_service.mapper.Mapper;
import com.example.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        User user = new User();
        user.setAuthId(userRequest.getAuthId());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(user.getUsername());

        User saveUser = userRepository.save(user);

        return mapper.mapToResponseDtoResponseDto(saveUser);
    }

    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapper.mapToResponseDtoResponseDto(user);
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));

        return mapper.mapToResponseDtoResponseDto(user);
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found" + email));
        return mapper.mapToResponseDtoResponseDto(user);
    }


    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u : users) {
            UserResponseDto userResponseDto = mapper.mapToResponseDtoResponseDto(u);
            userResponseDtos.add(userResponseDto);
        }
        return userResponseDtos;
    }

    public UserResponseDto updateUserDetails(String username,UserRequestDto requestDto){
        User user=userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("User not found"));

        user.setUsername(requestDto.getUsername());
        user.setName(requestDto.getName());
        user.set
    }


}



