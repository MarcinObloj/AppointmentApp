package com.financial.experts.module.appointment.controller;

import com.financial.experts.database.postgres.entity.Appointment;
import com.financial.experts.module.appointment.dto.AppointmentDTO;
import com.financial.experts.module.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Rezerwacje spotkań z ekspertami")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(
            summary = "Zarezerwuj spotkanie",
            description = "Pozwala użytkownikowi zarezerwować spotkanie z ekspertem",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Spotkanie zarezerwowane pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Błędne dane rezerwacji"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    public ResponseEntity<Appointment> reserveAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment = appointmentService.reserveAppointment(appointmentDTO);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/reserved")
    @Operation(
            summary = "Pobierz zarezerwowane spotkania",
            description = "Zwraca wszystkie zarezerwowane spotkania dla danego eksperta w wybranym dniu"
    )
    public ResponseEntity<List<Appointment>> getReservedAppointments(
            @Parameter(description = "ID eksperta", example = "5") @RequestParam int expertId,
            @Parameter(description = "Data w formacie YYYY-MM-DD", example = "2025-04-10") @RequestParam String date
    ) {
        List<Appointment> appointments = appointmentService.findAppointmentsByExpertAndDate(expertId, date);
        return ResponseEntity.ok(appointments);
    }
}
