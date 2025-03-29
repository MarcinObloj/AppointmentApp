package com.financial.experts.module.question.dto;

import lombok.Data;

@Data
public class QuestionRequestDTO {
    private Long clientId;
    private String content;
}