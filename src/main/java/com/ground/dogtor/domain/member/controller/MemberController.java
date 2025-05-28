package com.ground.dogtor.domain.member.controller;

import com.ground.dogtor.domain.member.dto.MemberLoginRequest;
import com.ground.dogtor.domain.member.dto.MemberLoginResponse;
import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/signup")
    public String signUpPage(){
        return "signUpPage";
    }

    @PostMapping("/signup")
    public String signUp(HttpServletRequest httpServletRequest, @ModelAttribute MemberSignUpRequest memberSignbUpRequest){
        memberService.signUp(memberSignbUpRequest);
        return "signUp";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest httpServletRequest, 
                       @ModelAttribute MemberLoginRequest memberLoginRequest,
                       HttpSession session,
                       HttpServletResponse response) {
        try {
            MemberLoginResponse loginResponse = memberService.login(memberLoginRequest);
            
            // 세션에 필요한 정보만 저장
            session.setAttribute("memberId", loginResponse.getMemberId());
            session.setAttribute("memberName", loginResponse.getName());
            
            // Access Token을 쿠키에 저장
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);  // JavaScript에서 접근 불가
            accessTokenCookie.setSecure(true);    // HTTPS에서만 전송
            accessTokenCookie.setPath("/");       // 모든 경로에서 접근 가능
            accessTokenCookie.setMaxAge(3600);    // 1시간 유효
            response.addCookie(accessTokenCookie);
            
            return "redirect:/"; // 메인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            return "redirect:/member/login?error=" + e.getMessage();
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 세션 무효화
        session.invalidate();
        
        // Access Token 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        
        return "redirect:/member/login";
    }
}
