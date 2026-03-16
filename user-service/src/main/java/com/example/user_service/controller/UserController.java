package com.example.user_service.controller;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get current logged-in user
    @Operation(
            summary = "Get current user profile",
            description = "Retrieve profile details of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {

        String username = authentication.getName();
        UserResponseDto user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    // Get user by ID (Admin or internal usage)
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user profile using the unique user ID. Accessible by users and administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "Unique ID of the user", example = "1")
            @PathVariable Integer id) {

        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Get user by username
    @Operation(
            summary = "Get user by username",
            description = "Retrieve a user profile using the unique username. Accessible by users and administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(
            @Parameter(description = "Unique username of the user", example = "john_doe")
            @PathVariable String username) {

        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // Get user by email
    @Operation(
            summary = "Get user by email",
            description = "Retrieve a user profile using the unique email address. Accessible by users and administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "Unique email of the user", example = "john_doe@example.com")
            @PathVariable String email) {

        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

// Get all users (Admin only)
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all user profiles. Accessible only by administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

// =============update=================


    public ResponseEntity<UserResponseDto>updateUser(Authentication authentication, @RequestBody UserRequestDto requestDto){
        String username=authentication.getName();

        UserResponseDto userResponseDto=userService.updateUserDetails(username,requestDto);

        return ResponseEntity.ok(userResponseDto);
    }

}