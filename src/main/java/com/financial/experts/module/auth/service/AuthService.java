package com.financial.experts.module.auth.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.financial.experts.appplication.security.JwtUtil;
import com.financial.experts.database.postgres.entity.*;
import com.financial.experts.database.postgres.repository.*;
import com.financial.experts.module.auth.dto.*;
import com.financial.experts.module.auth.exception.AuthException;
import com.financial.experts.module.expert.dto.ExpertDTO;
import com.financial.experts.module.mail.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final ExpertRepository expertRepository;
    private final SpecializationRepository specializationRepository;
    private final ExpertSpecializationRepository expertSpecializationRepository;
    private final Cloudinary cloudinary;

    public User register(RegisterDTO registerRequest) {
        validateEmailUniqueness(registerRequest.getEmail());

        User user = createUserFromRequest(registerRequest);
        uploadUserPhoto(user, registerRequest.getPhoto());

        User savedUser = userRepository.save(user);

        if ("EXPERT".equals(registerRequest.getRole())) {
            createExpertProfile(savedUser, registerRequest);
        }

        sendVerificationEmail(savedUser);
        return savedUser;
    }

    public LoginResponseDTO login(LoginDTO loginRequest, HttpServletResponse response) {
        authenticateUser(loginRequest);
        User user = getUserByEmail(loginRequest.getEmail());
        validateUserVerification(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Role użytkownika " + user.getEmail() + ":");
        authentication.getAuthorities().forEach(authority ->
                System.out.println("- " + authority.getAuthority())
        );

        String token = generateAndSetJwtCookie(user, response);
        return buildLoginResponse(user, token);
    }

    public CurrentUserDTO getCurrentUser(String email) {
        User user = getUserByEmail(email);
        return buildCurrentUserDTO(user);
    }

    public void logout(HttpServletResponse response) {
        clearJwtCookie(response);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AuthException("Użytkownik o podanym emailu już istnieje", HttpStatus.BAD_REQUEST);
        }
    }

    private User createUserFromRequest(RegisterDTO request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .verified(false)
                .build();
    }

    void uploadUserPhoto(User user, MultipartFile photo) {
        try {
            Map uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.emptyMap());
            user.setPhotoUrl(uploadResult.get("url").toString());
        } catch (IOException e) {
            throw new AuthException("Nie udało się przesłać zdjęcia", HttpStatus.BAD_REQUEST);
        }
    }

    void createExpertProfile(User user, RegisterDTO request) {
        Expert expert = buildExpert(user, request);
        expertRepository.save(expert);
        addExpertSpecializations(expert, request.getSpecializations());
    }

    private Expert buildExpert(User user, RegisterDTO request) {
        Expert expert = new Expert();
        expert.setUser(user);
        expert.setDescription(request.getDescription());
        expert.setExperienceYears(request.getExperienceYears());
        expert.setCity(request.getCity());
        expert.setStreet(request.getStreet());
        expert.setClientTypes(request.getClientTypes());
        expert.setAgeGroups(request.getAgeGroups());

        List<Service> services = request.getServices().stream()
                .map(serviceDTO -> new Service(serviceDTO.getName(), serviceDTO.getPrice()))
                .toList();
        expert.setServices(services);

        if (request.getWorkingHours() != null) {
            request.getWorkingHours().forEach(whDTO -> {
                WorkingHour wh = new WorkingHour();
                wh.setDayOfWeek(whDTO.getDayOfWeek());
                wh.setStartHour(whDTO.getStartHour());
                wh.setEndHour(whDTO.getEndHour());
                expert.addWorkingHour(wh);
            });
        }

        return expert;
    }

    private void addExpertSpecializations(Expert expert, List<Long> specializationIds) {
        specializationIds.forEach(id -> {
            Specialization specialization = specializationRepository.findById(id)
                    .orElseThrow(() -> new AuthException("Specjalizacja o ID " + id + " nie istnieje", HttpStatus.BAD_REQUEST));

            ExpertSpecialization expertSpecialization = new ExpertSpecialization();
            expertSpecialization.setExpert(expert);
            expertSpecialization.setSpecialization(specialization);
            expertSpecializationRepository.save(expertSpecialization);
        });
    }

    private void sendVerificationEmail(User user) {
        try {
            emailService.sendVerificationEmail(user);
        } catch (MessagingException e) {
            throw new AuthException("Nie udało się wysłać maila weryfikacyjnego", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void authenticateUser(LoginDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AuthException("Nieprawidłowy email lub hasło", HttpStatus.UNAUTHORIZED);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Użytkownik nie istnieje", HttpStatus.NOT_FOUND));
    }

    private void validateUserVerification(User user) {
        if (!user.isVerified()) {
            throw new AuthException("Konto nie zostało zweryfikowane", HttpStatus.UNAUTHORIZED);
        }
    }

    private String generateAndSetJwtCookie(User user, HttpServletResponse response) {
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        setJwtCookie(token, response);
        return token;
    }

    private void setJwtCookie(String token, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtUtil.getExpiration() / 1000));
        response.addCookie(jwtCookie);
    }

    private LoginResponseDTO buildLoginResponse(User user, String token) {
        Expert expert = "EXPERT".equals(user.getRole())
                ? expertRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AuthException("Expert not found", HttpStatus.NOT_FOUND))
                : null;

        return LoginResponseDTO.builder()
                .role(user.getRole())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .photoUrl(user.getPhotoUrl())
                .expert(expert)
                .build();
    }

    private CurrentUserDTO buildCurrentUserDTO(User user) {
        CurrentUserDTO dto = CurrentUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .photoUrl(user.getPhotoUrl())
                .build();

        if ("ROLE_EXPERT".equals(user.getRole())) {
            expertRepository.findByUserId(user.getId())
                    .ifPresent(expert -> {
                        ExpertDto expertDTO = ExpertDto.builder()
                                .id(expert.getId())
                                .description(expert.getDescription())
                                .experienceYears(expert.getExperienceYears())
                                .city(expert.getCity())
                                .street(expert.getStreet())
                                .clientTypes(expert.getClientTypes())
                                .ageGroups(expert.getAgeGroups())
                                .services(expert.getServices())
                                .workingHours(expert.getWorkingHours())
                                .build();
                        dto.setExpert(expertDTO);
                    });
        }

        return dto;
    }

    private void clearJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
    }
}