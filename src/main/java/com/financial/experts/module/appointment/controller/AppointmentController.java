package com.financial.experts.module.appointment.controller;

import com.financial.experts.database.postgres.entity.Appointment;
import com.financial.experts.module.appointment.dto.AppointmentDTO;
import com.financial.experts.module.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    public ResponseEntity<Appointment> reserveAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment = appointmentService.reserveAppointment(appointmentDTO);
        return ResponseEntity.ok(appointment);
    }
    @GetMapping("/reserved")
    public ResponseEntity<List<Appointment>> getReservedAppointments(@RequestParam int expertId,
                                                                     @RequestParam String date) {
        List<Appointment> appointments = appointmentService.findAppointmentsByExpertAndDate(expertId, date);
        return ResponseEntity.ok(appointments);
    }
}