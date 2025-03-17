package com.financial.experts.module.auth.service;

import com.financial.experts.appplication.security.JwtUtil;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.module.auth.dto.LoginDTO;
import com.financial.experts.module.auth.dto.RegisterDTO;
import com.financial.experts.module.auth.exception.LoginException;
import com.financial.experts.module.auth.exception.RegistrationException;
import com.financial.experts.module.mail.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() throws MessagingException {
        // Arrange
        RegisterDTO registerRequest = new RegisterDTO();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setRole("USER");

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword("encodedPassword");
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(registerRequest.getRole());
        user.setVerified(false);

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = authService.register(registerRequest);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.isVerified()).isFalse();

        verify(userRepository).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Arrange
        RegisterDTO registerRequest = new RegisterDTO();
        registerRequest.setEmail("test@example.com");

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Użytkownik o podanym emailu już istnieje");

        verify(userRepository).findByEmail(registerRequest.getEmail());
        verifyNoMoreInteractions(passwordEncoder, userRepository, emailService);
    }

    @Test
    void shouldThrowExceptionWhenEmailSendingFails() throws MessagingException {
        // Arrange
        RegisterDTO registerRequest = new RegisterDTO();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        doThrow(new MessagingException("Failed to send email")).when(emailService).sendVerificationEmail(any(User.class));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Nie udało się wysłać maila weryfikacyjnego");

        verify(userRepository).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(any(User.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(loginRequest.getEmail());

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setVerified(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn("mockedJwtToken");

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("mockedJwtToken");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(loginRequest.getEmail());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(jwtUtil).generateToken(loginRequest.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(LoginException.class)
                .hasMessage("Nieprawidłowy email lub hasło");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(userDetailsService, userRepository, jwtUtil);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password");

        // Symuluj pomyślne uwierzytelnienie (authenticationManager nie rzuca wyjątku)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Symuluj, że użytkownik nie istnieje w bazie danych
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Symuluj, że userDetailsService zwraca UserDetails (nawet jeśli użytkownik nie istnieje)
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(mockUserDetails);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(LoginException.class)
                .hasMessage("Użytkownik nie istnieje");

        // Weryfikuj, że metody zostały wywołane
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(userDetailsService).loadUserByUsername(loginRequest.getEmail()); // Teraz to wywołanie jest oczekiwane
        verifyNoInteractions(jwtUtil); // Upewnij się, że jwtUtil nie jest używany
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotVerified() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Symuluj pomyślne uwierzytelnienie (authenticationManager nie rzuca wyjątku)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Symuluj, że użytkownik istnieje, ale nie jest zweryfikowany
        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setVerified(false);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        // Symuluj, że userDetailsService zwraca UserDetails
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(mockUserDetails);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(LoginException.class)
                .hasMessage("Konto nie zostało zweryfikowane");

        // Weryfikuj, że metody zostały wywołane
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(userDetailsService).loadUserByUsername(loginRequest.getEmail()); // Teraz to wywołanie jest oczekiwane
        verifyNoInteractions(jwtUtil); // Upewnij się, że jwtUtil nie jest używany
    }
    }

