package com.example.quiz_service.dto;

import com.example.quiz_service.entity.Option;
import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {

    private String questionText;
    private String topic;
    private Difficulty difficulty;
    private String explanation;
    private Status status;
    private List<OptionRequest> options;
}