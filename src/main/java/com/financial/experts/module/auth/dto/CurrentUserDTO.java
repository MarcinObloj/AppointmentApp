package com.financial.experts.module.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String photoUrl;
    private ExpertDto expert;
}