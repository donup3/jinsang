package com.samplesecurity.controller;

import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignupPage(){
        return "/member/signup";
    }

    @GetMapping("/login")
    public String loginForm(String error, Model model) {
        log.info("login page loading done.");

        if (error != null) {
            model.addAttribute("error", "계정을 다시 확인해주세요.");
        }

        return "/member/login";
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

    @ResponseBody
    @PostMapping("/nicknameduplecheck")
    public ResponseEntity<Boolean> verifyNickName(String nickName) {
        boolean isNickNameExisted = memberService.nickNameChecker(nickName);
        if (isNickNameExisted) {
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }

    @PostMapping("/signup")
    public String grantSignup(MemberDto memberDto) {
        memberService.store(memberDto);
        return "redirect:/user/login";
    }
}
