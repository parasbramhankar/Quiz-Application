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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final Mapper mapper;

    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question = new Question();

        if (request.getOptions() != null && request.getOptions().size() != 4) {
            throw new IllegalArgumentException("Question must have exactly 4 options");
        }

        int countCorrect = 0;
        for (OptionRequest o : request.getOptions()) {
            if (o.isCorrect()) {
                countCorrect++;
            }
        }

        if (countCorrect != 1) {
            throw new IllegalArgumentException("Exactly one correct option required");
        }

        question.setQuestionText(request.getQuestionText());
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setExplanation(request.getExplanation());
        question.setStatus(request.getStatus());

        List<Option> optionList = new ArrayList<>();
        for (OptionRequest o : request.getOptions()) {
            Option option = new Option();
            option.setText(o.getText());
            option.setCorrect(o.isCorrect());

            option.setQuestion(question);
            optionList.add(option);
        }
        question.setOptions(optionList);
        Question saveQuestion = questionRepository.save(question);

        return mapper.mapToQuestionResponse(saveQuestion);
    }

    @Override
    public QuestionResponse updateQuestion(Integer questionId, UpdateQuestionRequest request) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException("Question not found"));

        if (request.getOptions() != null || request.getOptions().size() != 4) {
            throw new IllegalArgumentException("Question must have exactly 4 options");
        }

        int countCorrect = 0;

        for (OptionRequest o : request.getOptions()) {
            if (o.isCorrect()) {
                countCorrect++;
            }
        }

        if (countCorrect != 1) {
            throw new IllegalArgumentException("Exactly one correct option required");
        }

        //update basic field
        question.setQuestionText(request.getQuestionText());
        question.setTopic(request.getTopic());
        question.setDifficulty(request.getDifficulty());
        question.setExplanation(request.getExplanation());
        question.setStatus(request.getStatus());

        //Store existing options in the map
        Map<Integer, Option> existingOptions = new HashMap<>();
        if (question.getOptions() != null) {
            for (Option opt : question.getOptions()) {
                existingOptions.put(opt.getId(), opt);
            }
        }

        List<Option> updatedOptions = new ArrayList<>();
        for (OptionRequest opt : request.getOptions()) {

            // case 1:
            if (opt.getId() != null) {

                if (!existingOptions.containsKey(opt.getId())) {
                    throw new IllegalArgumentException("Invalid option id: " + opt.getId());
                }

                Option existing = existingOptions.get(opt.getId());
                existing.setText(opt.getText());
                existing.setCorrect(opt.isCorrect());

                updatedOptions.add(existing);

            }
            // case 2:
            else {
                Option newOption = new Option();
                newOption.setText(opt.getText());
                newOption.setCorrect(opt.isCorrect());
                newOption.setQuestion(question);

                updatedOptions.add(newOption);
            }
        }
        question.setOptions(updatedOptions);

        Question saveQuestion = questionRepository.save(question);


        return mapper.mapToQuestionResponse(saveQuestion);
    }

    @Override
    public void deleteQuestion(Integer questionId) {

        if (!questionRepository.existsById(questionId)) {
            throw new QuestionNotFoundException("Question not found");
        }

        questionRepository.deleteById(questionId);
    }

}
