package com.ground.dogtor.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignUpRequest {
    private String AdminLoginId;
    private String name;
    private String password;
    private String phoneNumber;
    private boolean smsAgree;
} 