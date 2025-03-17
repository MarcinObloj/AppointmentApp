package com.financial.experts.module.mail.service;

import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendVerificationEmail_ShouldSaveTokenAndSendEmail() throws MessagingException {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(templateEngine.process(eq("email-verification"), any(Context.class))).thenReturn("Email Content");

        // Act
        emailService.sendVerificationEmail(user);

        // Assert
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
        verify(emailSender, times(1)).send(mimeMessage);
    }
}