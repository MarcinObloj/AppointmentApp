package com.financial.experts.module.auth.dto;

import com.financial.experts.database.postgres.entity.Expert;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String role;
    private Long userId;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Expert expert; // Add this field
}
