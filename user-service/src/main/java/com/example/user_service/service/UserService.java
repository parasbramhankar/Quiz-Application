package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import com.example.user_service.exception.DuplicateResourceException;
import com.example.user_service.exception.EmailNotFoundException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.exception.UsernameNotFoundException;
import com.example.user_service.mapper.Mapper;
import com.example.user_service.repo.UserRepository;
import com.example.user_service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {


    private final UserRepository userRepository;
    private final Mapper mapper;
    private final FileStorageService fileStorageService;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());

        User saveUser = userRepository.save(user);

        return mapper.mapToResponseDto(saveUser);
    }

    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapper.mapToResponseDto(user);
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));

        return mapper.mapToResponseDto(user);
    }

    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found" + email));
        return mapper.mapToResponseDto(user);
    }


    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User u : users) {
            UserResponseDto userResponseDto = mapper.mapToResponseDto(u);
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
            throw new DuplicateResourceException("Username already taken");
        }

        if (requestDto.getEmail() != null &&
                !requestDto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
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

        User updatedUser = userRepository.save(user);
        return mapper.mapToResponseDto(updatedUser);
    }

    //delete current user
    public void deleteCurrentUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        fileStorageService.deleteFile(user.getProfilePic());
        userRepository.delete(user);
    }

    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
        fileStorageService.deleteFile(user.getProfilePic());
        userRepository.delete(user);
    }


    public UserResponseDto updateBio(String username, String bio) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (bio != null && bio.length() > 500) {
            throw new IllegalArgumentException("Bio cannot exceed 500 characters");
        }

        user.setBio(bio);

        User savedUser = userRepository.save(user);

        return mapper.mapToResponseDto(savedUser);
    }


    public UserResponseDto updateProfilePicture(String username, MultipartFile file) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Delete old file
        fileStorageService.deleteFile(user.getProfilePic());

        // Upload new file
        String fileUrl = fileStorageService.uploadProfilePicture(file);

        user.setProfilePic(fileUrl);

        User savedUser = userRepository.save(user);

        return mapper.mapToResponseDto(savedUser);
    }


}



