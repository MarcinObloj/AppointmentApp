package com.financial.experts.module.question.service;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.QuestionRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.module.question.dto.QuestionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    public Question addQuestion(QuestionRequestDTO requestDTO) {
        User client = userRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Question question = new Question();
        question.setClient(client);
        question.setContent(requestDTO.getContent());
        return questionRepository.save(question);
    }
}
