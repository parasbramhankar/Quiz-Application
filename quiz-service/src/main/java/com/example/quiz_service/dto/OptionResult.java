package com.example.quiz_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionResult {

    private Integer optionId;
    private String text;

    private boolean isCorrect;   // green
    private boolean isSelected;  // user choice
    private boolean isWrong;     // red
}