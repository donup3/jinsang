package com.samplesecurity.controller;

import com.samplesecurity.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/sendauthemail")
    public ResponseEntity<Boolean> sendVerificationEmail(String email) {
        log.info("email : " + email);
        boolean isEmailExisted = emailAuthService.emailChecker(email);
        if(isEmailExisted){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        emailAuthService.storeAuthCode(email);
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @PostMapping("/checkauthcode")
    public ResponseEntity<Boolean> verifyAuthCode(String authCode) {
        boolean isEmailAuthenticated = emailAuthService.authCodeChecker(authCode);
        if(isEmailAuthenticated){
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }
}
