package com.ground.dogtor.domain.member.service;

import com.ground.dogtor.domain.member.dao.MemberDAO;
import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.global.util.PasswordValidator;
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
}
