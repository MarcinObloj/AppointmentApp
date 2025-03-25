package com.financial.experts.module.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String description;
    private Integer experienceYears;
    private List<Long> specializations;
    private MultipartFile photo;
}