package com.samplesecurity.controller;

import com.samplesecurity.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    @GetMapping("/sendauthemail")
    public ResponseEntity<Boolean> sendVerificationEmail(String email) {
        log.info("email : " + email);
        boolean isExistedEmail = emailAuthService.emailChecker(email);
        if(isExistedEmail){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        emailAuthService.storeAuthCode(email);
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/checkauthcode")
    public ResponseEntity<Boolean> verifyAuthCode(String authCode) {
        boolean isAuthenticated = emailAuthService.authCodeChecker(authCode);
        if(isAuthenticated){
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }
}
