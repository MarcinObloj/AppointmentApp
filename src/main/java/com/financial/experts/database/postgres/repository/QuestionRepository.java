package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findTop5ByOrderByCreatedAtDesc();
}
