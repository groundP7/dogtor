package com.ground.dogtor.domain.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long id;
    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private boolean smsAgree;
    private String smsAgreedAt;
    private String createdAt;
} 