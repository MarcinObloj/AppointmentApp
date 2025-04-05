package com.financial.experts.module.user.controller;

import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users & Experts", description = "Operacje związane z użytkownikami i ekspertami")
public class UserController {

    private final UserService userService;

    @GetMapping("experts/{expertId}")
    @Operation(
            summary = "Pobierz eksperta z przypisanym użytkownikiem",
            description = "Zwraca eksperta wraz z danymi użytkownika na podstawie podanego ID eksperta"
    )
    public Expert getExpertWithUserById(@PathVariable Long expertId) {
        return userService.getExpertWithUserById(expertId);
    }

    @GetMapping("/experts")
    @Operation(
            summary = "Filtruj ekspertów po mieście i specjalizacji",
            description = "Zwraca listę ekspertów na podstawie podanego miasta i specjalizacji"
    )
    public List<Expert> getExpertsByCityAndSpecialization(
            @RequestParam String city,
            @RequestParam String specialization) {
        return userService.getExpertsByCityAndSpecialization(city, specialization);
    }
}
