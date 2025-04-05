package com.financial.experts.module.blog.controller;

import com.financial.experts.database.postgres.entity.Question;
import com.financial.experts.module.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
@Tag(name = "Blog", description = "Publiczna część bloga z pytaniami i odpowiedziami ekspertów")
public class BlogController {

    private final BlogService blogService;
    private final PagedResourcesAssembler<Question> pagedResourcesAssembler;

    @GetMapping("/questions")
    @Operation(
            summary = "Pobierz pytania z odpowiedziami",
            description = "Zwraca listę pytań, które mają przypisane odpowiedzi – z podziałem na strony"
    )
    public PagedModel<EntityModel<Question>> getAllQuestionsWithAnswers(
            @Parameter(description = "Numer strony", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Rozmiar strony (ilość elementów)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Question> pageResult = blogService.getAllQuestionsWithAnswers(page, size);
        return pagedResourcesAssembler.toModel(pageResult);
    }

    @GetMapping("/question/{id}")
    @Operation(
            summary = "Pobierz pytanie po ID",
            description = "Zwraca jedno pytanie wraz z odpowiedzią, jeśli istnieje"
    )
    public Question getQuestionById(
            @Parameter(description = "ID pytania", example = "123")
            @PathVariable Long id
    ) {
        return blogService.getQuestionById(id);
    }
}
