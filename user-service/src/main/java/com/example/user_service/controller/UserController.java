package com.example.user_service.controller;

import com.example.user_service.dto.BioRequestDto;
import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

// =============Update=================

    @Operation(
            summary = "Update user details",
            description = "Updates the authenticated user's profile details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UserRequestDto requestDto) {

        String username = authentication.getName(); // from JWT

        UserResponseDto response =
                userService.updateCurrentUser(username, requestDto);

        return ResponseEntity.ok(response);
    }

//Update bio
@Operation(
        summary = "Update user bio",
        description = "Updates the bio of the currently authenticated user"
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bio updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
})
@PreAuthorize("isAuthenticated()")
@PatchMapping("/me/bio")
public ResponseEntity<UserResponseDto> updateBio(
        Authentication authentication,
        @RequestBody BioRequestDto request) {

    String username = authentication.getName();

    UserResponseDto response =
            userService.updateBio(username, request.getBio());

    return ResponseEntity.ok(response);
}

// update profile pic
@Operation(
        summary = "Update profile picture",
        description = "Uploads and updates the profile picture of the authenticated user"
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile picture updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
})
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
@PatchMapping(value = "/me/profile-pic", consumes = "multipart/form-data")
public ResponseEntity<UserResponseDto> updateProfilePicture(
        Authentication authentication,
        @Parameter(description = "Profile image file (JPG or PNG)", required = true)
        @RequestPart("file") MultipartFile file) {

    String username = authentication.getName();

    return ResponseEntity.ok(
            userService.updateProfilePicture(username, file)
    );
}


//Delete current user

    @Operation(
            summary = "Delete current user account",
            description = "Deletes the account of the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {

        String username = authentication.getName();

        userService.deleteCurrentUser(username);

        return ResponseEntity.noContent().build();
    }

//admin delete the user
    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user account using the user ID (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
    
}