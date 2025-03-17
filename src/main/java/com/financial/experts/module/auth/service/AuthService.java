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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;


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

        User savedUser = userRepository.save(user);
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