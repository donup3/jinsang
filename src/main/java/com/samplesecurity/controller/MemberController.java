package com.samplesecurity.controller;

import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.dto.PasswordDto;
import com.samplesecurity.dto.ProfileDto;
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

    @ResponseBody
    @GetMapping("/get/profile")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication) {
        ProfileDto profileDto = memberService.getProfileImg(authentication);
        return ResponseEntity.ok().body(profileDto);
    }

    // 내 정보 페이지
    @GetMapping("/myinfo")
    public String displayMyInfo(Model model, Authentication authentication) {
        Member member = memberService.getMemberInfo(authentication);
        model.addAttribute("member", member);
        return "/member/myinfo";
    }

    @PostMapping("/checkNickName")
    public ResponseEntity<Boolean> verifyNickName(String nickName) {
        boolean isNickNameExisted = memberService.nickNameChecker(nickName);
        if (isNickNameExisted) {
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }

    @PostMapping("/change/nickname")
    public ResponseEntity<Boolean> changeNickName(String nickName, Authentication authentication) {
        Boolean isNickNameChanged = memberService.changeNickName(nickName, authentication);
        return ResponseEntity.ok().body(isNickNameChanged);
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
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordDto passwordDto, Authentication authentication) {
        Boolean isPasswordChanged =  memberService.changePassword(passwordDto, authentication);
        return ResponseEntity.ok().body(isPasswordChanged);
    }

    @PostMapping("/modify/profile")
    public ResponseEntity<Boolean> modifyProfile(@RequestBody ProfileDto profileDto, Authentication authentication) {
        memberService.modifyProfile(profileDto, authentication);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/delete/profile")
    public ResponseEntity<Boolean> deleteProfile(@RequestBody ProfileDto profileDto, Authentication authentication) {
        memberService.deleteProfile(profileDto, authentication);
        return ResponseEntity.ok().body(true);
    }
}
