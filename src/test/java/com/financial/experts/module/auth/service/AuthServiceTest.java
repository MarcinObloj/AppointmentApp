package com.financial.experts.module.auth.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.financial.experts.appplication.security.JwtUtil;
import com.financial.experts.database.postgres.entity.*;
import com.financial.experts.database.postgres.repository.*;
import com.financial.experts.module.auth.dto.*;
import com.financial.experts.module.auth.exception.AuthException;
import com.financial.experts.module.mail.service.EmailService;
import com.financial.experts.module.service.dto.ServiceDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;
    @Mock private UserDetailsService userDetailsService;
    @Mock private EmailService emailService;
    @Mock private ExpertRepository expertRepository;
    @Mock private SpecializationRepository specializationRepository;
    @Mock private ExpertSpecializationRepository expertSpecializationRepository;
    @Mock private Cloudinary cloudinary;
    @Mock private Uploader uploader;
    @Mock private HttpServletResponse response;
    @Mock private MultipartFile photo;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void register_ShouldSuccessfullyRegisterUser_WhenDataIsValid() throws IOException, MessagingException {
        // Arrange
        RegisterDTO registerRequest = new RegisterDTO();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setRole("USER");
        registerRequest.setPhoto(photo);
        registerRequest.setServices((Map<String, String>) Collections.emptyList()); // Dodane

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(uploader.upload(any(), any())).thenReturn(Map.of("url", "photoUrl"));
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Act
        User result = authService.register(registerRequest);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any());
        verify(emailService).sendVerificationEmail(any());
    }

    @Test
    void createExpertProfile_ShouldCreateExpertWithSpecializations() {
        // Arrange
        User user = new User();
        user.setId(1L);

        RegisterDTO request = new RegisterDTO();
        request.setSpecializations(List.of(1L, 2L));
        request.setRole("EXPERT");
        request.setServices(Map.of("Consulting", String.valueOf(100.0))); // Using Map instead of List
        request.setWorkingHours(List.of(new WorkingHourDTO())); // Proper WorkingHourDTO list

        Specialization spec1 = new Specialization();
        spec1.setId(1L);
        Specialization spec2 = new Specialization();
        spec2.setId(2L);

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(spec1));
        when(specializationRepository.findById(2L)).thenReturn(Optional.of(spec2));

        // Act
        authService.createExpertProfile(user, request);

        // Assert
        verify(expertRepository).save(any());
        verify(expertSpecializationRepository, times(2)).save(any());
    }

    @Test
    void buildExpert_ShouldHandleNullServicesAndWorkingHours() {
        // Arrange
        User user = new User();
        RegisterDTO request = new RegisterDTO();
        request.setSpecializations(List.of(1L));
        request.setRole("EXPERT");
        // services and workingHours are null

        Specialization spec = new Specialization();
        spec.setId(1L);
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(spec));

        // Act & Assert
        assertDoesNotThrow(() -> authService.createExpertProfile(user, request));
    }

    @Test
    void uploadUserPhoto_ShouldThrowException_WhenUploadFails() throws IOException {
        // Arrange
        User user = new User();
        when(uploader.upload(any(), any())).thenThrow(new IOException());

        // Act & Assert
        assertThrows(AuthException.class, () -> authService.uploadUserPhoto(user, photo));
    }



    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Arrange
        RegisterDTO registerRequest = new RegisterDTO();
        registerRequest.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.register(registerRequest));

        assertEquals("Użytkownik o podanym emailu już istnieje", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setVerified(true);
        user.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(), any())).thenReturn("jwtToken");
        when(jwtUtil.getExpiration()).thenReturn(3600000L);

        // Act
        LoginResponseDTO result = authService.login(loginRequest, response);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUserId());


        verify(response).addCookie(any(Cookie.class));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_ShouldThrowException_WhenUserNotVerified() {
        // Arrange
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setVerified(false);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.login(loginRequest, response));

        assertEquals("Konto nie zostało zweryfikowane", exception.getMessage());
    }

    @Test
    void getCurrentUser_ShouldReturnCurrentUserDTO() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setRole("EXPERT");

        Expert expert = new Expert();
        expert.setId(1L);
        expert.setDescription("Test expert");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));

        // Act
        CurrentUserDTO result = authService.getCurrentUser(email);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals(1L, result.getExpertId());
        assertEquals("Test expert", result.getDescription());
    }

    @Test
    void logout_ShouldClearJwtCookie() {
        // Act
        authService.logout(response);

        // Assert
        verify(response).addCookie(any(Cookie.class));
    }



}