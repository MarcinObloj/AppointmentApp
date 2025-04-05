package com.financial.experts.module.contact.controller;

import com.financial.experts.module.contact.dto.ContactDTO;
import com.financial.experts.module.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Formularz kontaktowy - wysyłanie wiadomości do administratora")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(
            summary = "Wyślij wiadomość kontaktową",
            description = "Pozwala użytkownikowi wysłać wiadomość kontaktową do administratora serwisu"
    )
    public ResponseEntity<String> sendContact(
            @RequestBody(
                    description = "Dane kontaktowe użytkownika i treść wiadomości",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContactDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ContactDTO contactDTO
    ) {
        contactService.sendContactEmail(contactDTO);
        return ResponseEntity.ok("Message sent successfully");
    }
}
