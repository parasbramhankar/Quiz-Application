package com.example.quiz_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionOnlyResponse {

    private Integer optionId;
    private String text;
}
