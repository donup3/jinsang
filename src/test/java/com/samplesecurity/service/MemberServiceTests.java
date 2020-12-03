package com.samplesecurity.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback
class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Test
    public void 비밀번호_리셋() {
        String email = "futuremaker019@naver.com";

        memberService.resetPassword(email);
    }
}