package com.financial.experts.module.question.controller;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.module.question.dto.QuestionRequestDTO;
import com.financial.experts.module.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "Operacje związane z pytaniami użytkowników")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(
            summary = "Dodaj pytanie",
            description = "Pozwala użytkownikowi dodać nowe pytanie skierowane do ekspertów"
    )
    public Question addQuestion(
            @org.springframework.web.bind.annotation.RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Obiekt zawierający dane pytania",
                    required = true,
                    content = @Content(schema = @Schema(implementation = QuestionRequestDTO.class))
            )
            QuestionRequestDTO request
    ) {
        return questionService.addQuestion(request);
    }
}
