package com.financial.experts.module.auth.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginDTO {
    private String email;
    private String password;
}

