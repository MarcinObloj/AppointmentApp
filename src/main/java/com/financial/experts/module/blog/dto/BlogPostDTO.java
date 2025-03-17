package com.financial.experts.module.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BlogPostDTO {
    private Long id;
    private Long expertId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
