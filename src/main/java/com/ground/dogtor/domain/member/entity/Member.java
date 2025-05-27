package com.ground.dogtor.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {

    private Long id;
    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private boolean smsAgree;
    private String smsAgreedAt;
    private String refreshToken;
    private String createdAt;

}
