package com.samplesecurity.service;

import com.samplesecurity.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Rollback(value = false)
class MemberDtoServiceTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 아이디생성() {
//        MemberDto memberDto = MemberDto.builder()
//                .email("noah00o@naver.com")
//                .password(passwordEncoder.encode("112233"))
//                .nickName("noah00o")
//                .build();

//        memberService.store(memberDto);

        //assertThat();
    }
}