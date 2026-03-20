package com.example.quiz_service.mapper;

import com.example.quiz_service.dto.OptionResponse;
import com.example.quiz_service.dto.QuestionResponse;
import com.example.quiz_service.entity.Option;
import com.example.quiz_service.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public QuestionResponse mapToQuestionResponse(Question question){
        QuestionResponse questionResponse = new QuestionResponse();

        questionResponse.setId(question.getId());
        questionResponse.setQuestionText(question.getQuestionText());
        questionResponse.setTopic(question.getTopic());
        questionResponse.setDifficulty(question.getDifficulty());
        questionResponse.setExplanation(question.getExplanation()); // ✅ fixed
        questionResponse.setStatus(question.getStatus());

        List<OptionResponse> optionResponseList = new ArrayList<>();

        if (question.getOptions() != null) {
            for (Option op : question.getOptions()) {
                OptionResponse optionResponse = new OptionResponse();

                optionResponse.setId(op.getId()); // ✅ added
                optionResponse.setText(op.getText());
                optionResponse.setCorrect(op.isCorrect());

                optionResponseList.add(optionResponse);
            }
        }

        questionResponse.setOptions(optionResponseList);

        return questionResponse;
    }




}
