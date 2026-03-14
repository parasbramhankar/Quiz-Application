package com.example.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private Integer authId;

    private String name;

    private String username;

    private String email;

}