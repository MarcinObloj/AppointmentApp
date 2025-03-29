package com.financial.experts.module.expert.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExpertDTO {
    private Long id;
    private String photoUrl;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
}
