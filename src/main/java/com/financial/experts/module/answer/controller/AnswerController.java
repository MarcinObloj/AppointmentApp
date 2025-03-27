
package com.financial.experts.module.answer.controller;

import com.financial.experts.database.postgres.entity.Answer;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.AnswerRepository;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ExpertRepository expertRepository;

    @PostMapping("/answer")
    public Answer answerQuestion(@RequestBody Answer answer, @RequestParam Long questionId, @RequestParam Long expertId) {
        logger.info("Received request to answer question with questionId: {} and expertId: {}", questionId, expertId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        answer.setQuestion(question);
        answer.setExpert(expert);

        return answerRepository.save(answer);
    }
}