package com.financial.experts.module.appointment.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentDTO {
    private Long id;
    private Long expertId;
    private Long clientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
