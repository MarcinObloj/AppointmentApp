package com.financial.experts.module.contact.dto;

import lombok.Data;

@Data
public class ContactDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
}