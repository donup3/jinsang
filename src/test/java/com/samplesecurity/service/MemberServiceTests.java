package com.samplesecurity.service;

import com.samplesecurity.domain.Member;
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

    @Test
    public void 타입에따른_ROLE저장() {
        String email = "futuremaker019@gmail.com";
        String password = "haha1122!!";
        String nickName = "noah00o";
        String type = "1";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .type(type)
                .build();
    }
}