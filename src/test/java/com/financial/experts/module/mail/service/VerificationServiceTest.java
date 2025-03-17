package com.financial.experts.module.mail.service;



import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.entity.VerificationToken;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.database.postgres.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VerificationService verificationService;



    @Test
    void verifyAccount_ShouldVerifyUserAccount() {
        // Arrange
        String token = "valid-token";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        User user = new User();
        user.setVerified(false);
        verificationToken.setUser(user);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        String result = verificationService.verifyAccount(token);

        // Assert
        assertThat(result).isEqualTo("Konto zostało pomyślnie zweryfikowane");
        assertThat(user.isVerified()).isTrue();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void verifyAccount_ShouldThrowExceptionWhenTokenIsInvalid() {
        // Arrange
        String token = "invalid-token";
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> verificationService.verifyAccount(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nieprawidłowy token weryfikacyjny");
    }

    @Test
    void verifyAccount_ShouldThrowExceptionWhenTokenIsExpired() {
        // Arrange
        String token = "expired-token";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().minusHours(1));

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act & Assert
        assertThatThrownBy(() -> verificationService.verifyAccount(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token weryfikacyjny wygasł");
    }
}