package com.samplesecurity.service;

import com.samplesecurity.domain.EmailAuth;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.MemberAuth;
import com.samplesecurity.domain.Profile;
import com.samplesecurity.dto.MemberAuthDto;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.repository.EmailAuthRepository;
import com.samplesecurity.repository.MemberAuthRepository;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final EmailAuthService emailAuthService;

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberAuthRepository memberAuthRepository;
    private final ProfileRepository profileRepository;


    public boolean nickNameChecker(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }

    public void store(MemberDto memberDto) {
        Member member = saveMember(memberDto);

        saveRoles(member);
        saveEmailAuth(memberDto, member);

        if (memberDto.getProfile() != null) {
            saveProfile(memberDto, member);
        }
    }

    public Member saveMember(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickName(memberDto.getNickName())
                .build();

        return memberRepository.save(member);
    }

    private void saveProfile(MemberDto memberDto, Member member) {
        Profile profile = memberDto.getProfile();
        profile.setMember(member);

        profileRepository.save(profile);
    }

    private void saveEmailAuth(MemberDto memberDto, Member member) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(memberDto.getEmail()).get();
        emailAuth.setMember(member);

        emailAuthRepository.save(emailAuth);
    }

    private void saveRoles(Member member) {
        List<MemberAuth> roles = new ArrayList<>();
        MemberAuth memberAuth = MemberAuth.builder().role("ROLE_MEMBER").build();
        roles.add(memberAuth);
        memberAuth.setMember(member);

        memberAuthRepository.save(memberAuth);
    }

    public void resetPassword(String email) {
        String generatedPassword = emailAuthService.sendAuthCode(email);

        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    member.setPassword(passwordEncoder.encode(generatedPassword));
                    memberRepository.save(member);
                });
    }
}
