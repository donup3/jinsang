package com.samplesecurity.service;

import com.samplesecurity.domain.EmailAuth;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.MemberAuth;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.repository.EmailAuthRepository;
import com.samplesecurity.repository.MemberAuthRepository;
import com.samplesecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService{

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberAuthRepository memberAuthRepository;

    public boolean nickNameChecker(String nickName) {
         return memberRepository.findByNickName(nickName).isPresent();
    }

    public Member saveMember(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickName(memberDto.getNickName())
                .build();

        return memberRepository.save(member);
    }

    public void store(MemberDto memberDto) {
        Member member = saveMember(memberDto);
        log.info("memberId : " + member.getId());

        EmailAuth emailAuth = emailAuthRepository.findByEmail(memberDto.getEmail()).get();
        emailAuth.setMember(member);
        emailAuthRepository.save(emailAuth);

        List<MemberAuth> roles = new ArrayList<>();
        MemberAuth memberAuth = MemberAuth.builder().role("ROLE_MEMBER").build();
        roles.add(memberAuth);

        memberAuth.setMember(member);

        memberAuthRepository.save(memberAuth);
    }

    public void saveProfileImg() {

    }

    public void saveRoles() {

    }
}
