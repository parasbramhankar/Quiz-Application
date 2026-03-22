package com.example.quiz_service.service.impl;

import com.example.quiz_service.dto.*;
import com.example.quiz_service.entity.Option;
import com.example.quiz_service.entity.Question;
import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import com.example.quiz_service.repository.QuestionRepository;
import com.example.quiz_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionRepository questionRepository;

    @Override
    public List<QuizQuestionResponse> generateQuiz(String topic, Difficulty difficulty, Integer limit) {

        List<Question> questions = new ArrayList<>();

        if (difficulty != null) {

            // ✅ Single difficulty
            List<Question> fetched = questionRepository
                    .findByTopicAndDifficultyAndStatus(topic, difficulty, Status.ACTIVE);

            if (fetched.size() < limit) {
                throw new RuntimeException("Not enough questions available");
            }

            Collections.shuffle(fetched);
            questions = fetched.stream().limit(limit).toList();

        } else {

            // 🔥 MIXED DIFFICULTY
            int easyCount = (int) (limit * 0.3);
            int mediumCount = (int) (limit * 0.4);
            int hardCount = limit - (easyCount + mediumCount);

            List<Question> easy = questionRepository
                    .findByTopicAndDifficultyAndStatus(topic, Difficulty.EASY, Status.ACTIVE);

            List<Question> medium = questionRepository
                    .findByTopicAndDifficultyAndStatus(topic, Difficulty.MEDIUM, Status.ACTIVE);

            List<Question> hard = questionRepository
                    .findByTopicAndDifficultyAndStatus(topic, Difficulty.HARD, Status.ACTIVE);

            if (easy.size() < easyCount || medium.size() < mediumCount || hard.size() < hardCount) {
                throw new RuntimeException("Not enough questions for mixed difficulty");
            }

            Collections.shuffle(easy);
            Collections.shuffle(medium);
            Collections.shuffle(hard);

            questions.addAll(easy.stream().limit(easyCount).toList());
            questions.addAll(medium.stream().limit(mediumCount).toList());
            questions.addAll(hard.stream().limit(hardCount).toList());

            // 🔥 FINAL SHUFFLE
            Collections.shuffle(questions);
        }

        // 🔄 MAP + OPTION SHUFFLE
        return questions.stream()
                .map(q -> {

                    List<Option> options = new ArrayList<>(q.getOptions());
                    Collections.shuffle(options);

                    return new QuizQuestionResponse(
                            q.getId(),
                            q.getQuestionText(),
                            options.stream()
                                    .map(opt -> new OptionOnlyResponse(
                                            opt.getId(),
                                            opt.getText()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    @Override
    public QuizResultResponse evaluateQuiz(
            List<Integer> questionIds,
            List<SubmitAnswerRequest> answers
    ) {

        if (questionIds == null || questionIds.isEmpty()) {
            throw new RuntimeException("Question list cannot be empty");
        }

        // Map answers
        Map<Integer, Integer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        SubmitAnswerRequest::getQuestionId,
                        SubmitAnswerRequest::getOptionId
                ));

        // 🔥 Fetch ALL questions
        List<Question> questions = questionRepository.findAllById(questionIds);

        int correctCount = 0;

        List<QuestionResult> results = new ArrayList<>();

        for (Question question : questions) {

            Integer selectedOptionId = answerMap.get(question.getId());
            boolean isAttempted = selectedOptionId != null;

            List<OptionResult> optionResults = new ArrayList<>();

            for (Option option : question.getOptions()) {

                boolean isSelected = option.getId().equals(selectedOptionId);
                boolean isCorrect = option.isCorrect();
                boolean isWrong = isSelected && !isCorrect;

                // ✔ Correct count
                if (isSelected && isCorrect) {
                    correctCount++;
                }

                optionResults.add(new OptionResult(
                        option.getId(),
                        option.getText(),
                        isCorrect,
                        isSelected,
                        isWrong
                ));
            }

            results.add(new QuestionResult(
                    question.getId(),
                    question.getQuestionText(),
                    optionResults,
                    question.getExplanation()
            ));
        }

        return new QuizResultResponse(
                questionIds.size(),
                correctCount,
                correctCount,
                results
        );
    }
}
