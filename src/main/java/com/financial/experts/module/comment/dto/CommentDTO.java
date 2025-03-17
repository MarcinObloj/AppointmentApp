package com.financial.experts.module.comment.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long expertId;
    private Long clientId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}