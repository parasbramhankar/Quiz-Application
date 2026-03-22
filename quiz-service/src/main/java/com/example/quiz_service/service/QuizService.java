package com.example.quiz_service.service;

import com.example.quiz_service.dto.QuizQuestionResponse;
import com.example.quiz_service.dto.QuizResultResponse;
import com.example.quiz_service.dto.SubmitAnswerRequest;
import com.example.quiz_service.entity.enums.Difficulty;

import java.util.List;

public interface QuizService {

    List<QuizQuestionResponse> generateQuiz(String topic, Difficulty difficulty, Integer limit);

    QuizResultResponse evaluateQuiz(List<Integer> questionIds, List<SubmitAnswerRequest> answers);


}
