package com.example.quiz_service.service.impl;

import com.example.quiz_service.dto.*;
import com.example.quiz_service.entity.Option;
import com.example.quiz_service.entity.Question;
import com.example.quiz_service.entity.QuizAttempt;
import com.example.quiz_service.entity.UserAnswer;
import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import com.example.quiz_service.repository.QuestionRepository;
import com.example.quiz_service.repository.QuizAttemptRepository;
import com.example.quiz_service.repository.UserAnswerRepository;
import com.example.quiz_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserAnswerRepository userAnswerRepository;

    //  Generate Quiz
    @Override
    public List<QuizQuestionResponse> generateQuiz(
            String topic,
            Difficulty difficulty,
            Integer limit
    ) {

        List<Question> questions;

        if (difficulty != null) {
            questions = questionRepository
                    .findByTopicAndDifficultyAndStatus(
                            topic, difficulty, Status.ACTIVE
                    );
        } else {
            questions = questionRepository
                    .findByTopicAndStatus(topic, Status.ACTIVE);
        }

        return questions.stream()
                .limit(limit)
                .map(q -> new QuizQuestionResponse(
                        q.getId(),
                        q.getQuestionText(),
                        q.getOptions().stream()
                                .map(o -> new OptionOnlyResponse(
                                        o.getId(),
                                        o.getText()
                                ))
                                .toList()
                ))
                .toList();
    }

    //  2. Evaluate Quiz + Save Attempt
    @Override

    public QuizResultResponse evaluateQuiz(
            List<Integer> questionIds,
            List<SubmitAnswerRequest> answers
    ) {

        // STEP 1: Get userId from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("User not authenticated");
        }

        Integer userId = Integer.parseInt((String) auth.getPrincipal());

        // STEP 2: Fetch questions
        List<Question> questions = questionRepository.findAllById(questionIds);

        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found");
        }

        Map<Integer, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        //  STEP 3: Build correct answer map
        Map<Integer, Integer> correctAnswerMap = new HashMap<>();

        for (Question q : questions) {
            Integer correctOptionId = q.getOptions().stream()
                    .filter(Option::isCorrect)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No correct option for question: " + q.getId()))
                    .getId();

            correctAnswerMap.put(q.getId(), correctOptionId);
        }

        // STEP 4: Convert user answers to map
        Map<Integer, Integer> userAnswersMap = answers.stream()
                .collect(Collectors.toMap(
                        SubmitAnswerRequest::getQuestionId,
                        SubmitAnswerRequest::getOptionId
                ));

        //  STEP 5: Evaluate
        int score = 0;
        List<QuestionResult> results = new ArrayList<>();

        for (Question q : questions) {

            Integer correctOptionId = correctAnswerMap.get(q.getId());
            Integer selectedOptionId = userAnswersMap.get(q.getId());

            boolean isCorrect = correctOptionId.equals(selectedOptionId);

            if (isCorrect) score++;

            List<OptionResult> optionResults = q.getOptions().stream()
                    .map(opt -> {
                        boolean selected = opt.getId().equals(selectedOptionId);
                        boolean correct = opt.isCorrect();
                        boolean wrong = selected && !correct;

                        return new OptionResult(
                                opt.getId(),
                                opt.getText(),
                                correct,
                                selected,
                                wrong
                        );
                    })
                    .toList();

            results.add(new QuestionResult(
                    q.getId(),
                    q.getQuestionText(),
                    optionResults,
                    q.getExplanation()
            ));
        }

        //  STEP 6: Save QuizAttempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(userId);
        attempt.setTopic(questions.get(0).getTopic()); // basic
        attempt.setDifficulty(questions.get(0).getDifficulty());
        attempt.setNumberOfQuestions(questions.size());
        attempt.setStartTime(LocalDateTime.now());
        attempt.setEndTime(LocalDateTime.now());
        attempt.setScore(score);

        quizAttemptRepository.save(attempt);

        //  STEP 7: Save User Answers
        for (SubmitAnswerRequest ans : answers) {

            Question question = questionMap.get(ans.getQuestionId());

            if (question == null) continue;

            Option selectedOption = question.getOptions().stream()
                    .filter(o -> o.getId().equals(ans.getOptionId()))
                    .findFirst()
                    .orElse(null);

            UserAnswer ua = new UserAnswer();
            ua.setAttempt(attempt);
            ua.setQuestion(question);
            ua.setSelectedOption(selectedOption);

            userAnswerRepository.save(ua);
        }

        // STEP 8: Return Response
        return new QuizResultResponse(
                questions.size(),
                score,
                score, // can scale later
                results
        );
    }
}