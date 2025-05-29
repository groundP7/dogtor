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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(3600);
            response.addCookie(accessTokenCookie);
            
            return "redirect:/";
        } catch (Exception e) {
            // URL 인코딩 처리
            try {
                String encodedError = java.net.URLEncoder.encode(e.getMessage(), "UTF-8");
                return "redirect:/member/login?error=" + encodedError;
            } catch (java.io.UnsupportedEncodingException ex) {
                return "redirect:/member/login?error=로그인에 실패했습니다";
            }
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

    @GetMapping("/find-id")
    public String findIdPage() {
        return "findId";
    }

    @PostMapping("/find-id")
    public String findId(@RequestParam String name, 
                        @RequestParam String phoneNumber,
                        Model model) {
        try {
            String foundId = memberService.findId(name, phoneNumber);
            model.addAttribute("foundId", foundId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "findId";
    }

    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "findPassword";
    }

    @PostMapping("/find-password")
    public String findPassword(@RequestParam String loginId,
                             @RequestParam String phoneNumber,
                             Model model) {
        try {
            // 사용자 확인
            if (memberService.verifyUser(loginId, phoneNumber)) {
                // 비밀번호 재설정 페이지로 이동
                model.addAttribute("loginId", loginId);
                model.addAttribute("phoneNumber", phoneNumber);
                return "resetPassword";
            } else {
                model.addAttribute("errorMessage", "입력하신 정보와 일치하는 회원이 없습니다.");
                return "findPassword";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "findPassword";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String loginId,
                              @RequestParam String phoneNumber,
                              @RequestParam String newPassword,
                              Model model,
                              HttpServletResponse response) {
        try {
            memberService.updatePassword(loginId, phoneNumber, newPassword);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": true}");
            return null;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("loginId", loginId);
            model.addAttribute("phoneNumber", phoneNumber);
            return "resetPassword";
        }
    }
}
