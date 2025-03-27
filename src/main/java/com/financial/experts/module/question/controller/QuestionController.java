package com.financial.experts.module.question.controller;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.financial.experts.database.postgres.entity.User;
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionRepository questionRepository;
    @PostMapping("/ask")
    public Question askQuestion(@RequestBody Question question, @RequestParam Long clientId) {
        User client = new User();
        client.setId(clientId);
        question.setClient(client);
        return questionRepository.save(question);
    }
}
