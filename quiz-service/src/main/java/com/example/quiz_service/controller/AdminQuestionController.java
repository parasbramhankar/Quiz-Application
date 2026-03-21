package com.example.quiz_service.controller;

import com.example.quiz_service.dto.CreateQuestionRequest;
import com.example.quiz_service.dto.QuestionResponse;
import com.example.quiz_service.dto.UpdateQuestionRequest;
import com.example.quiz_service.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "Add a new question",
            description = "Creates a new question in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only ADMIN allowed")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<QuestionResponse> addQuestion(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Question details to be created",
                    required = true
            )
            @Parameter(description = "CreateQuestionRequest object containing question, options, correct answer, difficulty, etc.")
            @RequestBody @Valid CreateQuestionRequest request
    ) {
        QuestionResponse questionResponse = questionService.createQuestion(request);
        return ResponseEntity.status(201).body(questionResponse);
    }


//Update


    @Operation(
            summary = "Update existing question",
            description = "Updates a question in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only ADMIN allowed"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(

            @Parameter(
                    description = "ID of the question to update",
                    required = true
            )
            @PathVariable Integer id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated question details",
                    required = true
            )
            @Parameter(
                    description = "UpdateQuestionRequest object containing updated question data"
            )
            @Valid @RequestBody UpdateQuestionRequest updateQuestionRequest
    ) {
        QuestionResponse questionResponse =
                questionService.updateQuestion(id, updateQuestionRequest);

        return ResponseEntity.ok(questionResponse);
    }



//Delete Question
    @Operation(
            summary = "Delete existing question",
            description = "Delete a question in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only ADMIN allowed"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteQuestion(
            @Parameter(
                      description = "ID of the question to delete",
                      required = true
            )
            @PathVariable Integer id
    ){
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted Successfully");
    }



}
