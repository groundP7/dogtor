package com.ground.dogtor.domain.member.controller;

import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/signup")
    public String signUp(HttpServletRequest httpServletRequest, @ModelAttribute MemberSignUpRequest memberSignbUpRequest){

        memberService.signUp(memberSignbUpRequest);

        return "jsp/signUp";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
