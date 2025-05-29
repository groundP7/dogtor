package com.ground.dogtor.domain.member.service;

import com.ground.dogtor.domain.member.dao.MemberDAO;
import com.ground.dogtor.domain.member.dto.MemberLoginRequest;
import com.ground.dogtor.domain.member.dto.MemberLoginResponse;
import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.domain.member.entity.Member;
import com.ground.dogtor.global.config.JwtConfig;
import com.ground.dogtor.global.util.PasswordValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MemberService {

    @Autowired
    MemberDAO memberDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtConfig jwtConfig;

    @Transactional
    // 회원가입 처리 메서드
    public void signUp(MemberSignUpRequest memberSignUpRequest){

        // 1. 회원가입 요청 데이터 유효성 검사.
        // 1-1 필수 값을 체크한다.
        if (!StringUtils.hasText(memberSignUpRequest.getLoginId())) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getName())) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("전화번호는 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getAddress())) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getDetailAddress())) {
            throw new IllegalArgumentException("상세주소는 필수입니다.");
        } else if (!StringUtils.hasText(memberSignUpRequest.getPostalCode())) {
            throw new IllegalArgumentException("우편번호는 필수입니다.");
        }

        // 1-2 아이디 중복 체크
        if (memberDAO.existsByLoginId(memberSignUpRequest.getLoginId()) >= 1) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        // 1-3 입력한 비밀번호 형식 체크
        if (memberSignUpRequest.getPassword().length() < 8){
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        } else if (memberSignUpRequest.getPassword().length() > 20) {
            throw new RuntimeException("비밀번호는 20자 이하이어야 합니다.");
        } else if (!PasswordValidator.isValidPassword(memberSignUpRequest.getPassword())){
            throw new RuntimeException("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }

        // 2. 비밀번호 암호화
        memberSignUpRequest.setPassword(passwordEncoder.encode(memberSignUpRequest.getPassword()));

        // 3. 회원가입 요청 멤버 데이터를 db에 저장 후 memberId 추출
        Long memberId = memberDAO.signUpMember(memberSignUpRequest);

        // 4. 회원가입 요청 주소 데이터를 db에 저장
        memberDAO.signUpAddress(memberId, memberSignUpRequest);
    }

    @Transactional
    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        // 1. 입력값 검증
        if (memberLoginRequest.getLoginId() == null || memberLoginRequest.getLoginId().isEmpty()) {
            throw new RuntimeException("아이디를 입력하세요.");
        }

        if (memberLoginRequest.getPassword() == null || memberLoginRequest.getPassword().isEmpty()) {
            throw new RuntimeException("비밀번호를 입력하세요.");
        }

        // 2. 회원 조회
        Member member = memberDAO.findByLoginId(memberLoginRequest);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        // 3. 비밀번호 확인
        if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 4. 토큰 생성
        // Access Token 생성
        String accessToken = jwtConfig.createAccessToken(member.getId());
        // Refresh Token 생성
        String refreshToken = jwtConfig.createRefreshToken(member.getId());

        // 5. Refresh Token을 DB에 저장
        memberDAO.updateRefreshToken(member.getId(), refreshToken);

        // 6. 응답 데이터 생성 및 반환
        return MemberLoginResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
