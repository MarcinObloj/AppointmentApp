package com.financial.experts.module.mail.service;

import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class VerificationService {


    private final VerificationTokenRepository tokenRepository;


    private final UserRepository userRepository;

    public ModelAndView verifyAccount(String token) {
        // Sprawdzanie tokena weryfikacyjnego
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy token weryfikacyjny"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token weryfikacyjny wygasł");
        }

        // Weryfikacja użytkownika
        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);


        ModelAndView modelAndView = new ModelAndView("verification-success");
        modelAndView.addObject("name", user.getFirstName());
        modelAndView.addObject("verificationLink", "http://localhost:4200/login");
        return modelAndView;
    }
}