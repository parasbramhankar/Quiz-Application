package com.example.quiz_service.service.impl;

import com.example.quiz_service.dto.CreateQuestionRequest;
import com.example.quiz_service.dto.OptionRequest;
import com.example.quiz_service.dto.QuestionResponse;
import com.example.quiz_service.dto.UpdateQuestionRequest;
import com.example.quiz_service.entity.Option;
import com.example.quiz_service.entity.Question;
import com.example.quiz_service.exception.QuestionNotFoundException;
import com.example.quiz_service.mapper.Mapper;
import com.example.quiz_service.repository.QuestionRepository;
import com.example.quiz_service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final Mapper mapper;

    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question=new Question();

        question.setQuestionText(request.getQuestionText());
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setExplanation(request.getExplanation());
        question.setStatus(request.getStatus());

        List<Option>optionList=new ArrayList<>();
        for(OptionRequest o: request.getOptions()) {
            Option option = new Option();
            option.setText(o.getText());
            option.setCorrect(o.isCorrect());

            option.setQuestion(question);
            optionList.add(option);
        }
        question.setOptions(optionList);
        Question saveQuestion=questionRepository.save(question);

        return mapper.mapToQuestionResponse(saveQuestion);
    }

    @Override
    public QuestionResponse updateQuestion(Integer questionId, UpdateQuestionRequest request) {
        Question question=questionRepository.findById(questionId).orElseThrow(()->
                new QuestionNotFoundException("Question not found"));

        question.setQuestionText(request.getQuestionText());
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setExplanation(request.getExplanation());
        question.setStatus(request.getStatus());

        List<Option>optionList=new ArrayList<>();
        for(OptionRequest o: request.getOptions()) {
            Option option = new Option();
            option.setText(o.getText());
            option.setCorrect(o.isCorrect());

            option.setQuestion(question);
            optionList.add(option);
        }
        question.setOptions(optionList);
        Question saveQuestion=questionRepository.save(question);

        return mapper.mapToQuestionResponse(saveQuestion);
    }

    @Override
    public void deleteQuestion(Integer questionId) {

    }

}
