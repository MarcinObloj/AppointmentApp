package com.financial.experts.module.mail.controller;

import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import com.financial.experts.module.mail.service.VerificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VerificationController {


    private final VerificationService verificationService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
     String message = verificationService.verifyAccount(token);
     return ResponseEntity.ok(message);
}
}