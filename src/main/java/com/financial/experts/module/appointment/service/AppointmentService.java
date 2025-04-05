package com.financial.experts.module.appointment.service;

import com.financial.experts.database.postgres.entity.Appointment;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.AppointmentRepository;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.module.appointment.dto.AppointmentDTO;
import com.financial.experts.module.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public Appointment reserveAppointment(AppointmentDTO dto) throws Exception {
        Expert expert = expertRepository.findById(dto.getExpertId())
                .orElseThrow(() -> new Exception("Expert not found"));
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new Exception("Client not found"));
        Appointment appointment = new Appointment();
        appointment.setExpert(expert);
        appointment.setClient(client);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        Appointment savedAppointment = appointmentRepository.save(appointment);


        emailService.sendAppointmentConfirmation(client, expert, savedAppointment);
        return savedAppointment;
    }
    public List<Appointment> findAppointmentsByExpertAndDate(int expertId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        return appointmentRepository.findByExpertIdAndAppointmentDate(Long.valueOf(expertId), localDate);
    }
}