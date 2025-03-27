package com.financial.experts.module.blog.controller;


import com.financial.experts.database.postgres.entity.Answer;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.AnswerRepository;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final QuestionRepository questionRepository;


    private final AnswerRepository answerRepository;


    private final ExpertRepository expertRepository;


    @GetMapping("/questions")
    public List<Question> getAllQuestionsWithAnswers() {
        return questionRepository.findAll();
    }


    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }
}