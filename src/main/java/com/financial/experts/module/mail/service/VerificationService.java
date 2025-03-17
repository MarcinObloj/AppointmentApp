package com.financial.experts.module.mail.service;

import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class VerificationService {


    private final VerificationTokenRepository tokenRepository;


    private final UserRepository userRepository;

    public String verifyAccount(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy token weryfikacyjny"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token weryfikacyjny wygasł");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        return "Konto zostało pomyślnie zweryfikowane";
    }
}