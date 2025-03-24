package com.financial.experts.module.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String description;
    private int experienceYears;
    private List<Long> specializations;
}