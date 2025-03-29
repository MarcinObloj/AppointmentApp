package com.financial.experts.module.answer.dto;


import com.financial.experts.module.expert.dto.ExpertDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private ExpertDTO expert;
}