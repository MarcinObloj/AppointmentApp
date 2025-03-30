package com.financial.experts.module.auth.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkingHourDTO {
    private String dayOfWeek;
    private String startHour;
    private String endHour;
}