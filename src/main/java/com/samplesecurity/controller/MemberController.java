package com.samplesecurity.controller;

import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.dto.PasswordDto;
import com.samplesecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignupPage(@RequestParam(required = false) String type, Model model) {
        model.addAttribute("type", type);
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myinfo")
    public String displayMyInfo(Model model, Authentication authentication) {
        Member member = memberService.getMemberInfo(authentication);
        model.addAttribute("member", member);
        return "/member/myinfo";
    }

    @ResponseBody
    @PostMapping("/checkNickName")
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

    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(String email) {
        memberService.resetPassword(email);
        return ResponseEntity.ok().body("인증코드 전송 완료");
    }


    @PostMapping("/match/password")
    public ResponseEntity<Boolean> matchPassword(String currentPassword, Authentication authentication) {
        Boolean isPasswordVerified = memberService.matchPassword(currentPassword, authentication);
        return ResponseEntity.ok().body(isPasswordVerified);
    }

    @PostMapping("/password")
    public ResponseEntity<String> password(PasswordDto passwordDto, Authentication authentication) {
        memberService.changePassword(passwordDto, authentication);
        return ResponseEntity.ok().body("비밀번호 변경 완료");
    }
}
