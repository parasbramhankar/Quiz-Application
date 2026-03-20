package com.example.quiz_service.entity;

import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String questionText;

    private String topic;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String explanation;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;
}