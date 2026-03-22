package com.example.quiz_service.repository;

import com.example.quiz_service.entity.Question;
import com.example.quiz_service.entity.enums.Difficulty;
import com.example.quiz_service.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {

        List<Question> findByTopicAndDifficultyAndStatus(
                String topic,
                Difficulty difficulty,
                Status status
        );

        List<Question> findByTopicAndStatus(
                String topic,
                Status status
        );

        @Query(value = """
        SELECT * FROM question 
        WHERE topic = :topic
        AND (:difficulty IS NULL OR difficulty = :difficulty)
        AND status = 'ACTIVE'
        ORDER BY RAND()
        LIMIT :limit
    """, nativeQuery = true)
        List<Question> findRandomQuestions(
                String topic,
                String difficulty,
                int limit
        );
}
