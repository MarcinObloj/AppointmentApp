package com.financial.experts.module.auth.dto;

import com.financial.experts.database.postgres.entity.Service;
import com.financial.experts.database.postgres.entity.WorkingHour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertDto {
    private Long id;
    private String description;
    private Integer experienceYears;
    private String city;
    private String street;
    private List<String> clientTypes;
    private List<String> ageGroups;
    private List<Service> services;
    private List<WorkingHour> workingHours;
}