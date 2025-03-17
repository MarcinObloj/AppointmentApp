package com.financial.experts.module.rating.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long id;
    private Long expertId;
    private Long clientId;
    private Integer rating;
    private String comment;
}
