package com.ground.dogtor.domain.member.controller;

import com.ground.dogtor.domain.member.dto.MemberLoginRequest;
import com.ground.dogtor.domain.member.dto.MemberLoginResponse;
import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.domain.member.entity.Member;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signUpPage(){
        return "signUpPage";
    }

    // 회원가입
    @PostMapping("/signup")
    public String signUp(HttpServletRequest httpServletRequest, @ModelAttribute MemberSignUpRequest memberSignbUpRequest){
        memberService.signUp(memberSignbUpRequest);
        return "signUp";
    }

    // 로그인
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

    // 로그아웃 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    // 로그아웃
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

    // 아이디 찾기 페이지
    @GetMapping("/find-id")
    public String findIdPage() {
        return "findId";
    }

    // 아이디 찾기
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

    // 비밀번호 찾기 페이지
    @GetMapping("/find-password")
    public String findPasswordPage() {
        return "findPassword";
    }

    // 비밀번호 찾기
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

    // 비밀번호 수정
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

    // 프로필 페이지
    @GetMapping("/my-profile")
    public String myProfile(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/member/login";
        }
        
        try {
            Member member = memberService.getMemberById(memberId);
            Map<String, String> address = memberService.getMemberAddress(memberId);
            
            model.addAttribute("member", member);
            model.addAttribute("memberAddress", address);
            return "myPage";
        } catch (Exception e) {
            return "redirect:/member/login";
        }
    }

    // 프로필 수정
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam String name,
            @RequestParam String phoneNumber,
            @RequestParam String postalCode,
            @RequestParam String address,
            @RequestParam String detailAddress,
            HttpSession session,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return null;
        }

        try {
            memberService.updateProfile(memberId, currentPassword, newPassword, name, phoneNumber,
                                     postalCode, address, detailAddress);
            session.setAttribute("memberName", name);
            
            response.getWriter().write("{\"success\": true, \"message\": \"회원 정보가 성공적으로 수정되었습니다.\"}");
            return null;
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage().replace("\"", "'") + "\"}");
            return null;
        }
    }

    // 회원탈퇴
    @PostMapping("/delete-account")
    public void deleteAccount(
            @RequestBody Map<String, String> request,
            HttpSession session,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return;
        }

        String password = request.get("password");
        if (password == null || password.trim().isEmpty()) {
            response.getWriter().write("{\"success\": false, \"message\": \"비밀번호를 입력해주세요.\"}");
            return;
        }

        try {
            memberService.deleteAccount(memberId, password);
            session.invalidate();
            
            // Access Token 쿠키 삭제
            Cookie accessTokenCookie = new Cookie("accessToken", null);
            accessTokenCookie.setMaxAge(0);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);
            
            response.getWriter().write("{\"success\": true, \"message\": \"회원 탈퇴가 완료되었습니다.\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }


}
