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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Map;

@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

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
        try {
            // 1. 입력값 검증
            if (memberLoginRequest.getLoginId() == null || memberLoginRequest.getLoginId().isEmpty()) {
                String errorMsg = "아이디를 입력하세요.";
                logger.error("[로그인 실패] {}", errorMsg);
                throw new RuntimeException(errorMsg);
            }

            if (memberLoginRequest.getPassword() == null || memberLoginRequest.getPassword().isEmpty()) {
                String errorMsg = "비밀번호를 입력하세요.";
                logger.error("[로그인 실패] {}", errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 2. 회원 조회
            Member member = memberDAO.findByLoginId(memberLoginRequest);
            if (member == null) {
                String errorMsg = "존재하지 않는 아이디입니다.";
                logger.error("[로그인 실패] {} - 입력된 아이디: {}", errorMsg, memberLoginRequest.getLoginId());
                throw new RuntimeException(errorMsg);
            }

            // 3. 비밀번호 확인
            if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
                String errorMsg = "비밀번호가 일치하지 않습니다.";
                logger.error("[로그인 실패] {} - 아이디: {}", errorMsg, memberLoginRequest.getLoginId());
                throw new IllegalArgumentException(errorMsg);
            }

            // 4. 토큰 생성
            String accessToken = jwtConfig.createAccessToken(member.getId());
            String refreshToken = jwtConfig.createRefreshToken(member.getId());

            // 5. Refresh Token을 DB에 저장
            memberDAO.updateRefreshToken(member.getId(), refreshToken);

            logger.info("[로그인 성공] 사용자: {}", memberLoginRequest.getLoginId());

            // 6. 응답 데이터 생성 및 반환
            return MemberLoginResponse.builder()
                    .memberId(member.getId())
                    .name(member.getName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            logger.error("[로그인 오류] 메시지: {}", e.getMessage());
            logger.debug("[로그인 오류] 상세 정보:", e);  // 스택 트레이스는 DEBUG 레벨로 출력
            throw e;
        }
    }

    public String findId(String name, String phoneNumber) {

        // 입력한 정보를 이용해서 아이디를 찾는다.
        Member member = memberDAO.findByNameAndPhoneNumber(name, phoneNumber);
        if (member == null) {
            throw new RuntimeException("입력하신 정보와 일치하는 회원이 없습니다.");
        }
        return member.getLoginId();
    }

    public boolean verifyUser(String loginId, String phoneNumber) {
        Member member = memberDAO.findByLoginIdAndPhoneNumber(loginId, phoneNumber);
        return member != null;
    }

    @Transactional
    public void updatePassword(String loginId, String phoneNumber, String newPassword) {
        // 1. 사용자 확인
        Member member = memberDAO.findByLoginIdAndPhoneNumber(loginId, phoneNumber);
        if (member == null) {
            throw new RuntimeException("입력하신 정보와 일치하는 회원이 없습니다.");
        }

        // 2. 비밀번호 유효성 검사
        if (newPassword.length() < 8) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        } else if (newPassword.length() > 20) {
            throw new RuntimeException("비밀번호는 20자 이하이어야 합니다.");
        } else if (!PasswordValidator.isValidPassword(newPassword)) {
            throw new RuntimeException("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        // 4. DB에 새 비밀번호 저장
        memberDAO.updatePassword(member.getId(), encodedPassword);
        
        logger.info("[비밀번호 변경 성공] 사용자: {}", loginId);
    }

    public Member getMemberById(Long memberId) {
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        return member;
    }

    public Map<String, String> getMemberAddress(Long memberId) {
        return memberDAO.findAddressByMemberId(memberId);
    }

    @Transactional
    public void updateProfile(Long memberId, String currentPassword, String newPassword, 
                            String name, String phoneNumber, String postalCode, 
                            String address, String detailAddress) {
        // 1. 회원 조회
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        // 2. 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 3. 새 비밀번호가 있는 경우 유효성 검사
        String encodedPassword = null;
        if (newPassword != null && !newPassword.isEmpty()) {
            if (newPassword.length() < 8) {
                throw new RuntimeException("새 비밀번호는 8자 이상이어야 합니다.");
            } else if (newPassword.length() > 20) {
                throw new RuntimeException("새 비밀번호는 20자 이하이어야 합니다.");
            } else if (!PasswordValidator.isValidPassword(newPassword)) {
                throw new RuntimeException("새 비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
            }
            encodedPassword = passwordEncoder.encode(newPassword);
        }

        // 4. 회원 정보 업데이트
        memberDAO.updateProfile(memberId, name, phoneNumber, encodedPassword);
        
        // 5. 주소 정보 업데이트
        memberDAO.updateAddress(memberId, postalCode, address, detailAddress);
        
        logger.info("[회원 정보 수정 성공] 회원 ID: {}", memberId);
    }
}
