package com.example.quiz_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestionResponse {

    private Integer questionId;
    private String questionText;
    private List<OptionOnlyResponse> options;
}