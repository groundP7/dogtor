package com.ground.dogtor.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberSignUpRequest {

    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private boolean smsAgree;

    private String address;
    private String detailAddress;
    private String postalCode;
}
