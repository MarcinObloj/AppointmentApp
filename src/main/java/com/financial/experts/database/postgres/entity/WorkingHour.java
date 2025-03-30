package com.financial.experts.database.postgres.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "working_hours")
@Getter
@Setter
@NoArgsConstructor
public class WorkingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Day of week e.g. MONDAY, TUESDAY...
    @Column(nullable = false)
    private String dayOfWeek;

    // Start time of the appointment, e.g. "08:00"
    @Column(nullable = false)
    private String startHour;

    // End time of the appointment, e.g. "16:00"
    @Column(nullable = false)
    private String endHour;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    @JsonBackReference
    private Expert expert;
}