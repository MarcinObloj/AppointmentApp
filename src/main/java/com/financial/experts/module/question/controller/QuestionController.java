package com.financial.experts.module.question.controller;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import com.financial.experts.module.question.dto.QuestionRequestDTO;
import com.financial.experts.module.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.financial.experts.database.postgres.entity.User;
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public Question addQuestion(@RequestBody QuestionRequestDTO request) {
        return questionService.addQuestion(request);
    }
}
