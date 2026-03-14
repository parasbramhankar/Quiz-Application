package com.example.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private Integer authId;

    private String name;

    private String username;

    private String email;

    private String bio;

    private String profilePic;

    private Integer points;
}