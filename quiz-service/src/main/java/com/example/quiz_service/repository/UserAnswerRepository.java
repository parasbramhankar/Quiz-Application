package com.example.quiz_service.repository;

import com.example.quiz_service.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
}
