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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        User user = new User();
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


//Update user-details
public UserResponseDto updateCurrentUser(String username, UserRequestDto requestDto) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    // Check duplicates safely
    if (requestDto.getUsername() != null &&
            !requestDto.getUsername().equals(user.getUsername()) &&
            userRepository.existsByUsername(requestDto.getUsername())) {
        throw new RuntimeException("Username already taken");
    }

    if (requestDto.getEmail() != null &&
            !requestDto.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(requestDto.getEmail())) {
        throw new RuntimeException("Email already in use");
    }

    // Update only allowed fields
    if (requestDto.getName() != null) {
        user.setName(requestDto.getName());
    }
    if (requestDto.getUsername() != null) {
        user.setUsername(requestDto.getUsername());
    }
    if (requestDto.getEmail() != null) {
        user.setEmail(requestDto.getEmail());
    }

    return mapper.mapToResponseDtoResponseDto(user);
    }

//delete current user
    public void deleteCurrentUser(String username){
        User user=userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public void deleteUserById(Integer id){
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("user not found"));
        userRepository.delete(user);
    }

    public UserResponseDto updateBio(String username, String bio) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Optional validation
        if (bio != null && bio.length() > 500) {
            throw new RuntimeException("Bio cannot exceed 500 characters");
        }

        user.setBio(bio);

        return mapper.mapToResponseDtoResponseDto(user);
    }


    public UserResponseDto updateProfilePicture(String username, MultipartFile file) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new RuntimeException("Only JPG and PNG files are allowed");
        }

        //  Generate unique file name
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        //Define path
        Path filePath = Paths.get("uploads/profile-pics/" + fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }

        // Save file URL (not file itself)
        String fileUrl = "/uploads/profile-pics/" + fileName;

        user.setProfilePic(fileUrl);

        return mapper.mapToResponseDtoResponseDto(user);
    }





}



