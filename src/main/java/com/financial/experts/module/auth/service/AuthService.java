package com.financial.experts.module.auth.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.financial.experts.appplication.security.JwtUtil;
import com.financial.experts.database.postgres.entity.Expert;
import com.financial.experts.database.postgres.entity.ExpertSpecialization;
import com.financial.experts.database.postgres.entity.Specialization;
import com.financial.experts.database.postgres.entity.User;
import com.financial.experts.database.postgres.repository.ExpertRepository;
import com.financial.experts.database.postgres.repository.ExpertSpecializationRepository;
import com.financial.experts.database.postgres.repository.SpecializationRepository;
import com.financial.experts.database.postgres.repository.UserRepository;
import com.financial.experts.module.auth.dto.LoginDTO;
import com.financial.experts.module.auth.dto.RegisterDTO;
import com.financial.experts.module.auth.exception.LoginException;
import com.financial.experts.module.auth.exception.RegistrationException;
import com.financial.experts.module.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
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

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RegistrationException("Użytkownik o podanym emailu już istnieje");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(registerRequest.getRole());
        user.setVerified(false);
        try {
            Map uploadResult = cloudinary.uploader().upload(registerRequest.getPhoto().getBytes(), ObjectUtils.emptyMap());
            user.setPhotoUrl(uploadResult.get("url").toString());
        } catch (IOException e) {
            throw new RegistrationException("Nie udało się przesłać zdjęcia");
        }
        User savedUser = userRepository.save(user);
        if ("EXPERT".equals(registerRequest.getRole())) {
            Expert expert = new Expert();
            expert.setUser(savedUser);
            expert.setDescription(registerRequest.getDescription());

            expert.setExperienceYears(registerRequest.getExperienceYears());
            expertRepository.save(expert);
            List<Long> specializations = registerRequest.getSpecializations();
            for (Long specializationId : specializations) {
                Specialization specialization = specializationRepository.findById(specializationId)
                        .orElseThrow(() -> new RegistrationException("Specjalizacja o ID " + specializationId + " nie istnieje"));
                ExpertSpecialization expertSpecialization = new ExpertSpecialization();
                expertSpecialization.setExpert(expert);
                expertSpecialization.setSpecialization(specialization);
                expertSpecializationRepository.save(expertSpecialization);
            }
        }

        try {
            emailService.sendVerificationEmail(user);
        } catch (MessagingException e) {
            throw new RegistrationException("Nie udało się wysłać maila weryfikacyjnego");
        }

        return savedUser;
    }
    public String login(LoginDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new LoginException("Nieprawidłowy email lub hasło");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());


        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new LoginException("Użytkownik nie istnieje"));
        if (!user.isVerified()) {
            throw new LoginException("Konto nie zostało zweryfikowane");
        }

        return jwtUtil.generateToken(userDetails.getUsername());
    }
}