package com.example.quiz_service.service;

import com.example.quiz_service.dto.CreateQuestionRequest;
import com.example.quiz_service.dto.QuestionResponse;
import com.example.quiz_service.dto.UpdateQuestionRequest;

public interface QuestionService {

    QuestionResponse createQuestion(CreateQuestionRequest request);

    QuestionResponse updateQuestion(Integer questionId, UpdateQuestionRequest request);

    void deleteQuestion(Integer questionId);
}
