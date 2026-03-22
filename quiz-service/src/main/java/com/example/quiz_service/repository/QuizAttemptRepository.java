package com.example.quiz_service.repository;

import com.example.quiz_service.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {

    // optional (future use)
    List<QuizAttempt> findByUserId(Integer userId);
}