package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.expert.id = :expertId AND a.startTime BETWEEN :start AND :end")
    List<Appointment> findAppointmentsByExpertAndDateRange(
            @Param("expertId") Long expertId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
