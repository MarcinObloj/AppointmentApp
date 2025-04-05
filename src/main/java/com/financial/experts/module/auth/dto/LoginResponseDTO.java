package com.financial.experts.module.auth.dto;

import com.financial.experts.database.postgres.entity.Expert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String role;
    private Long userId;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Expert expert; // Add this field
}
