package com.example.quiz_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponse {

    private Integer id;
    private String text;
    private boolean isCorrect;
}