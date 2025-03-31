// file: src/main/java/com/financial/experts/database/postgres/repository/AppointmentRepository.java
package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByExpertIdAndAppointmentDate(Long expertId, LocalDate appointmentDate);
}