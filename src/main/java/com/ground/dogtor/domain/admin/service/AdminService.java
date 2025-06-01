package com.ground.dogtor.domain.admin.service;

import com.ground.dogtor.domain.admin.dao.AdminDAO;
import com.ground.dogtor.domain.admin.dto.AdminLoginRequest;
import com.ground.dogtor.domain.admin.dto.AdminLoginResponse;
import com.ground.dogtor.domain.admin.dto.AdminSignUpRequest;
import com.ground.dogtor.domain.admin.entity.Admin;
import com.ground.dogtor.global.config.JwtConfig;
import com.ground.dogtor.global.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Transactional
    public void signUp(AdminSignUpRequest request) {
        // 1. 입력값 검증
        if (!StringUtils.hasText(request.getAdminLoginId())) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (!StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            throw new IllegalArgumentException("전화번호는 필수입니다.");
        }

        // 2. 아이디 중복 체크
        if (adminDAO.existsByLoginId(request.getAdminLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 3. 비밀번호 유효성 검사
        if (request.getPassword().length() < 8 || request.getPassword().length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8~20자 사이여야 합니다.");
        }
        if (!PasswordValidator.isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }

        // 4. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 5. 관리자 정보 저장
        Admin admin = new Admin();
        admin.setAdminLoginId(request.getAdminLoginId());
        admin.setName(request.getName());
        admin.setPassword(encodedPassword);
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setSmsAgree(request.isSmsAgree());
        if (request.isSmsAgree()) {
            admin.setSmsAgreedAt(LocalDateTime.now().toString());
        }

        adminDAO.save(admin);
    }

    @Transactional
    public AdminLoginResponse login(AdminLoginRequest request) {
        try {
            // 1. 입력값 검증
            if (!StringUtils.hasText(request.getAdminLoginId())) {
                String errorMsg = "아이디를 입력하세요.";
                logger.error("[로그인 실패] {}", errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            if (!StringUtils.hasText(request.getPassword())) {
                String errorMsg = "비밀번호를 입력하세요.";
                logger.error("[로그인 실패] {}", errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // 2. 관리자 조회
            Admin admin = adminDAO.findByLoginId(request.getAdminLoginId());
            if (admin == null) {
                String errorMsg = "존재하지 않는 아이디입니다.";
                logger.error("[로그인 실패] {} - 입력된 아이디: {}", errorMsg, request.getAdminLoginId());
                throw new IllegalArgumentException(errorMsg);
            }

            // 3. 비밀번호 확인
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                String errorMsg = "비밀번호가 일치하지 않습니다.";
                logger.error("[로그인 실패] {} - 아이디: {}", errorMsg, request.getAdminLoginId());
                throw new IllegalArgumentException(errorMsg);
            }

            // 4. 토큰 생성
            String accessToken = jwtConfig.createAccessToken(admin.getId());
            String refreshToken = jwtConfig.createRefreshToken(admin.getId());

            // 5. Refresh Token을 DB에 저장
            adminDAO.updateRefreshToken(admin.getId(), refreshToken);

            logger.info("[로그인 성공] 관리자: {}", request.getAdminLoginId());

            // 6. 응답 데이터 생성 및 반환
            return AdminLoginResponse.builder()
                    .adminId(admin.getId())
                    .name(admin.getName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            logger.error("[로그인 오류] 메시지: {}", e.getMessage());
            logger.debug("[로그인 오류] 상세 정보:", e);
            throw e;
        }
    }
} 