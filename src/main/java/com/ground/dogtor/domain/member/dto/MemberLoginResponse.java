package com.ground.dogtor.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberLoginResponse {

    private Long memberId;
    private String name;
    private String accessToken;
    private String refreshToken;

}
