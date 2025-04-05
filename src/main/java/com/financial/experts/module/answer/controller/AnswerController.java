package com.financial.experts.module.answer.controller;

import com.financial.experts.database.postgres.entity.Answer;
import com.financial.experts.module.answer.dto.AnswerResponseDto;
import com.financial.experts.module.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
@Tag(name = "Answers", description = "Operacje związane z odpowiedziami na pytania")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/answer")
    @Operation(
            summary = "Dodaj odpowiedź do pytania",
            description = "Pozwala ekspertowi dodać odpowiedź na konkretne pytanie.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Answer.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Odpowiedź została dodana pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Błędne dane odpowiedzi"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono pytania lub eksperta")
            }
    )
    public AnswerResponseDto answerQuestion(
            @RequestBody(description = "Dane odpowiedzi ekspertów") Answer answer,
            @Parameter(description = "ID pytania, na które ekspert udziela odpowiedzi", example = "123") @RequestParam Long questionId,
            @Parameter(description = "ID eksperta udzielającego odpowiedzi", example = "456") @RequestParam Long expertId
    ) {
        return answerService.createAnswer(answer, questionId, expertId);
    }
}
