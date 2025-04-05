package com.financial.experts.module.appointment.service;

import com.financial.experts.database.postgres.entity.*;
import com.financial.experts.database.postgres.repository.*;
import com.financial.experts.module.appointment.dto.AppointmentDTO;
import com.financial.experts.module.mail.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ExpertRepository expertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void reserveAppointment_ShouldSuccessfullyCreateAppointment_WhenDataIsValid() throws Exception {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setExpertId(1L);
        dto.setClientId(1L);
        dto.setAppointmentDate(LocalDate.now().plusDays(1));
        dto.setAppointmentTime(String.valueOf(LocalTime.of(14, 0)));

        Expert expert = new Expert();
        expert.setId(1L);
        User client = new User();
        client.setId(1L);
        client.setEmail("client@example.com");

        when(expertRepository.findById(1L)).thenReturn(Optional.of(expert));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment a = invocation.getArgument(0);
            a.setId(1L); // Simulate saved entity
            return a;
        });

        // Act
        Appointment result = appointmentService.reserveAppointment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(expert, result.getExpert());
        assertEquals(client, result.getClient());
        assertEquals(dto.getAppointmentDate(), result.getAppointmentDate());
        assertEquals(dto.getAppointmentTime(), result.getAppointmentTime());

        verify(emailService, times(1)).sendAppointmentConfirmation(client, expert, result);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void reserveAppointment_ShouldThrowException_WhenExpertNotFound() {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setExpertId(1L);
        when(expertRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () ->
                appointmentService.reserveAppointment(dto));

        assertEquals("Expert not found", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void reserveAppointment_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        AppointmentDTO dto = new AppointmentDTO();
        dto.setExpertId(1L);
        dto.setClientId(1L);

        when(expertRepository.findById(1L)).thenReturn(Optional.of(new Expert()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () ->
                appointmentService.reserveAppointment(dto));

        assertEquals("Client not found", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void findAppointmentsByExpertAndDate_ShouldReturnList_WhenAppointmentsExist() {
        // Arrange
        Long expertId = 1L;
        String date = "2023-01-01";
        LocalDate localDate = LocalDate.parse(date);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDate(localDate);

        when(appointmentRepository.findByExpertIdAndAppointmentDate(expertId, localDate))
                .thenReturn(Collections.singletonList(appointment));

        // Act
        List<Appointment> result = appointmentService.findAppointmentsByExpertAndDate(expertId.intValue(), date);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(appointmentRepository, times(1))
                .findByExpertIdAndAppointmentDate(expertId, localDate);
    }

    @Test
    void findAppointmentsByExpertAndDate_ShouldReturnEmptyList_WhenNoAppointments() {
        // Arrange
        Long expertId = 1L;
        String date = "2023-01-01";
        LocalDate localDate = LocalDate.parse(date);

        when(appointmentRepository.findByExpertIdAndAppointmentDate(expertId, localDate))
                .thenReturn(Collections.emptyList());

        // Act
        List<Appointment> result = appointmentService.findAppointmentsByExpertAndDate(expertId.intValue(), date);

        // Assert
        assertTrue(result.isEmpty());
        verify(appointmentRepository, times(1))
                .findByExpertIdAndAppointmentDate(expertId, localDate);
    }
}