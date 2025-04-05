package com.financial.experts.module.answer.service;

import com.financial.experts.database.postgres.entity.Answer;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.repository.AnswerRepository;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import com.financial.experts.module.answer.dto.AnswerResponseDto;
import com.financial.experts.module.answer.exception.AnswerException;
import com.financial.experts.module.expert.dto.ExpertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ExpertRepository expertRepository;

    @Transactional
    public AnswerResponseDto createAnswer(Answer answer, Long questionId, Long expertId) {
        Question question = getQuestionById(questionId);
        Expert expert = getExpertById(expertId);

        answer.setQuestion(question);
        answer.setExpert(expert);
        Answer savedAnswer = answerRepository.save(answer);

        return mapToResponseDto(savedAnswer);
    }

    private Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new AnswerException("Question not found", HttpStatus.NOT_FOUND));
    }

    private Expert getExpertById(Long expertId) {
        return expertRepository.findById(expertId)
                .orElseThrow(() -> new AnswerException("Expert not found", HttpStatus.NOT_FOUND));
    }

    AnswerResponseDto mapToResponseDto(Answer answer) {
        Expert expert = answer.getExpert();
        ExpertDTO expertDto = buildExpertDto(expert);

        return AnswerResponseDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .expert(expertDto)
                .build();
    }

    private ExpertDTO buildExpertDto(Expert expert) {
        ExpertDTO expertDto = new ExpertDTO();
        expertDto.setId(expert.getId());
        expertDto.setStreet(expert.getStreet());
        expertDto.setCity(expert.getCity());

        if (expert.getUser() != null) {
            expertDto.setPhotoUrl(expert.getUser().getPhotoUrl());
            expertDto.setFirstName(expert.getUser().getFirstName());
            expertDto.setLastName(expert.getUser().getLastName());
        } else {
            expertDto.setPhotoUrl("");
            expertDto.setFirstName("Unknown");
            expertDto.setLastName("Expert");
        }

        return expertDto;
    }
}