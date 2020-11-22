package com.samplesecurity.controller;

import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String index(){
        return "/index";
    }

    @GetMapping("/user/signup")
    public String signupGet(){
        return "/signup";
    }

    @PostMapping("/user/signup")
    public String signupPost(MemberDto memberDto){
        memberService.joinUser(memberDto);

        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String loginForm() {
        return "/login";
    }


    @GetMapping("/user/login/result")
    public String dispLoginResult() {

        return "/loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "/logout";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String dispDenied() {
        return "/denied";
    }

    // 내 정보 페이지
    @GetMapping("/user/info")
    public String dispMyInfo() {
        return "/myinfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/admin";
    }
}
