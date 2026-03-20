package com.example.quiz_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResult {

    private Integer questionId;
    private String questionText;
    private List<OptionResult> options;
    private String explanation;
}