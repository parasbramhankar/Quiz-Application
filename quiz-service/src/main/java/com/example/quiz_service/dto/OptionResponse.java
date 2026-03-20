package com.example.quiz_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponse {

    private String text;
    private boolean isCorrect;
}