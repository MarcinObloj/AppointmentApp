package com.financial.experts.module.contact.controller;

import com.financial.experts.module.contact.dto.ContactDTO;
import com.financial.experts.module.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> sendContact(@RequestBody ContactDTO contactDTO) {
        contactService.sendContactEmail(contactDTO);
        return ResponseEntity.ok("Message sent successfully");
    }
}