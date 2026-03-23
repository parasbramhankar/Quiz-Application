package com.example.quiz_service.entity;

import com.example.quiz_service.entity.enums.Difficulty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz_attempt")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String topic;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Integer numberOfQuestions;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer score;
}

