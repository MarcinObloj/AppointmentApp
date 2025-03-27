package com.financial.experts.module.expert.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExpertDTO {
    private String description;
    private Long id;
   private Integer experienceYears;
    private List<String> specializations;
    private List<String> languages;
    private List<String> locations;
}
