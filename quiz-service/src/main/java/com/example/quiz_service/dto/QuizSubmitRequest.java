package com.example.quiz_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmitRequest {

    private List<Integer>questionIds;
    private List<SubmitAnswerRequest>answers;
}
