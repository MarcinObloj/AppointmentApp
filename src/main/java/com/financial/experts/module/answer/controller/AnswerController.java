
package com.financial.experts.module.answer.controller;

import com.financial.experts.database.postgres.entity.Answer;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.AnswerRepository;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import com.financial.experts.module.answer.dto.AnswerResponseDto;
import com.financial.experts.module.expert.dto.ExpertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public AnswerResponseDto answerQuestion(@RequestBody Answer answer, @RequestParam Long questionId, @RequestParam Long expertId) {
        logger.info("Received request to answer question with questionId: {} and expertId: {}", questionId, expertId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        answer.setQuestion(question);
        answer.setExpert(expert);
        Answer savedAnswer = answerRepository.save(answer);

        return mapToDto(savedAnswer);
    }

    private AnswerResponseDto mapToDto(Answer answer) {
        AnswerResponseDto dto = new AnswerResponseDto();
        dto.setId(answer.getId());
        dto.setContent(answer.getContent());
        dto.setCreatedAt(answer.getCreatedAt());


        Expert expert = expertRepository.findById(answer.getExpert().getId())
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        ExpertDTO expertDto = new ExpertDTO();
        expertDto.setId(expert.getId());


        if(expert.getUser() != null) {
            expertDto.setPhotoUrl(expert.getUser().getPhotoUrl());
            expertDto.setFirstName(expert.getUser().getFirstName());
            expertDto.setLastName(expert.getUser().getLastName());
        } else {
            expertDto.setPhotoUrl("");
            expertDto.setFirstName("Unknown");
            expertDto.setLastName("Expert");
        }

        expertDto.setStreet(expert.getStreet());
        expertDto.setCity(expert.getCity());
        dto.setExpert(expertDto);

        return dto;
    }
}