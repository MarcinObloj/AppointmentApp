package com.financial.experts.module.auth.controller;

import com.financial.experts.module.auth.dto.CurrentUserDTO;
import com.financial.experts.module.auth.dto.LoginDTO;
import com.financial.experts.module.auth.dto.LoginResponseDTO;
import com.financial.experts.module.auth.dto.RegisterDTO;
import com.financial.experts.module.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails == null
                ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                : ResponseEntity.ok(authService.getCurrentUser(userDetails.getUsername()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}