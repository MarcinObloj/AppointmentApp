package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long>{
    List<Answer> findByQuestionId(Long questionId);

}
