package com.example.quiz_service.dto;

import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizRequest {

    private String title;

    private String topic;

    private String description;

    private Integer timeLimit;

    private Difficulty difficulty;

    private Status status;

    //Adding questions id, not question,
    //There may be n number of question
    private List<Integer> questionIds;
}