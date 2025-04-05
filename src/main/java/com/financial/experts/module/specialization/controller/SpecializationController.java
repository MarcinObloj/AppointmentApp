package com.financial.experts.module.specialization.controller;

import com.financial.experts.database.postgres.entity.Specialization;
import com.financial.experts.module.specialization.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
@Tag(name = "Specializations", description = "Operacje związane ze specjalizacjami ekspertów finansowych")
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping
    @Operation(
            summary = "Pobierz wszystkie specjalizacje",
            description = "Zwraca listę wszystkich dostępnych specjalizacji przypisanych do ekspertów"
    )
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        List<Specialization> specializations = specializationService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }
}
