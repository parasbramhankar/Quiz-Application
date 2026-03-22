package com.example.quiz_service.controller;

import com.example.quiz_service.dto.*;
import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // 1. Generate Quiz
    @Operation(
            summary = "Generate dynamic quiz",
            description = "Generates quiz based on topic, optional difficulty, and number of questions. " +
                    "If difficulty is not provided, mixed difficulty questions are returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @GetMapping
    public ResponseEntity<List<QuizQuestionResponse>> generateQuiz(

            @Parameter(
                    description = "Topic name (e.g., Java, DBMS)",
                    required = true,
                    example = "Java"
            )
            @RequestParam @NotBlank String topic,

            @Parameter(
                    description = "Difficulty level (EASY, MEDIUM, HARD). Optional for mixed quiz",
                    example = "EASY"
            )
            @RequestParam(required = false) Difficulty difficulty,

            @Parameter(
                    description = "Number of questions",
                    required = true,
                    example = "10"
            )
            @RequestParam @Min(1) @Max(50) Integer limit
    ) {

        List<QuizQuestionResponse> quiz =
                quizService.generateQuiz(topic, difficulty, limit);

        return ResponseEntity.ok(quiz);
    }

    // 2. Submit Quiz
    @Operation(
            summary = "Submit quiz and evaluate result",
            description = "Evaluates submitted answers. Unattempted questions are treated as incorrect. " +
                    "Returns detailed result with correct/wrong answers and explanations."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz evaluated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid submission data")
    })
    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Quiz submission containing questionIds and answers",
                    required = true
            )
            @Parameter(
                    description = "QuizSubmitRequest object containing questionIds and selected answers"
            )
            @Valid @RequestBody QuizSubmitRequest request
    ) {

        QuizResultResponse result = quizService.evaluateQuiz(
                request.getQuestionIds(),
                request.getAnswers()
        );

        return ResponseEntity.ok(result);
    }
}