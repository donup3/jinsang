package com.samplesecurity.service;

import com.samplesecurity.domain.*;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.dto.PasswordDto;
import com.samplesecurity.dto.ProfileDto;
import com.samplesecurity.repository.EmailAuthRepository;
import com.samplesecurity.repository.MemberAuthRepository;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final FileService fileService;

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
                .type(memberDto.getType())
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
        addRole(Role.MEMBER.getValue(), member, roles);

        if (member.getType().equals("1"))
            addRole(Role.LAWYER.getValue(), member, roles);

        if (member.getType().equals("2"))
            addRole(Role.CS.getValue(), member, roles);

        if (member.getType().equals("3"))
            addRole(Role.COUNSELOR.getValue(), member, roles);
    }

    private void addRole(String specificRole, Member member, List<MemberAuth> roles) {
        MemberAuth memberAuth = MemberAuth.builder().role(specificRole).build();

        memberAuth.setMember(member);
        roles.add(memberAuth);
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

    public Boolean changePassword(PasswordDto passwordDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        memberRepository.findByEmail(userEmail)
            .ifPresent(member -> {
                member.setPassword(passwordEncoder.encode(passwordDto.getEditPassword()));
                memberRepository.save(member);
            });

        return true;
    }

    public Boolean matchPassword(String currentPassword, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        Member member = memberRepository.findByEmail(userEmail).get();
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            return false;
        }
        return true;
    }

    public Member getMemberInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        return memberRepository.findByEmail(userEmail).get();
    }

    public Boolean changeNickName(String nickName, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        memberRepository.findByEmail(userEmail)
                .ifPresent(member -> {
                    member.setNickName(nickName);
                    memberRepository.save(member);
                });
        return true;
    }

    public void modifyProfile(ProfileDto profileDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        memberRepository.findByEmail(userEmail)
            .ifPresent(member -> {
                // 회원가입시 프로필을 저장했을때
                if (member.getProfile() != null) {
                    Profile memberProfile = member.getProfile();
                    memberProfile.setUuid(profileDto.getUuid());
                    memberProfile.setUploadPath(profileDto.getUploadPath());
                    memberProfile.setFileName(profileDto.getFileName());
                    profileRepository.save(memberProfile);
                // 회원가입시 프로필을 저장하지 않았을때
                } else {
//                    Profile memberProfile = member.getProfile();
//                    member.setProfile(profile);
//                    member.getProfile().setMember(member);
//                    memberRepository.save(member);

                    Profile profile = profileDto.toEntity();
                    profile.setMember(member);
                    profileRepository.save(profile);
                }
            });
    }

    public ProfileDto getProfileImg(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        // todo : profile이 없을때 null에 대한 처리 필요
        Member member = memberRepository.findByEmail(userEmail).get();
        Profile profile = member.getProfile();

        Profile memberProfile = profileRepository.findByUuid(profile.getUuid())
                .orElseThrow(() -> new NullPointerException("this member has no profile img."));

        ProfileDto profileDto = ProfileDto.builder()
                .uuid(memberProfile.getUuid())
                .uploadPath(memberProfile.getUploadPath())
                .fileName(memberProfile.getFileName())
                .build();

        return profileDto;
    }

    public void deleteProfile(ProfileDto profileDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        memberRepository.findByEmail(userEmail)
            .ifPresent(member -> {
                Profile profile = profileRepository.findByUuid(profileDto.getUuid()).get();
                profileRepository.delete(profile);
                fileService.deleteProfile(profileDto);
            });
    }
}
