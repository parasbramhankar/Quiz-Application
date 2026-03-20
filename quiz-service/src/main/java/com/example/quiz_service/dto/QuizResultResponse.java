package com.example.quiz_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultResponse {

    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer score;
    private List<QuestionResult> results;
}