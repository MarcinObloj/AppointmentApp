package com.financial.experts.module.appointment.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long expertId;
    private Long clientId;
    private LocalDate appointmentDate;
    private String appointmentTime;
}
