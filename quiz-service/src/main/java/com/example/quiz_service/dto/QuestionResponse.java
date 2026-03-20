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
public class QuestionResponse {

    private Integer id;
    private String questionText;
    private String topic;
    private Difficulty difficulty;
    private String explanation;
    private Status status;
    private List<OptionResponse> options;
}