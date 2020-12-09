package com.samplesecurity.security.domain;

import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.MemberDto;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Getter
public class CustomUser extends User {

    private static final long serialVersionUID = 1L;

    private Member member;

    public CustomUser(Member member) {
        super(member.getEmail(), member.getPassword(), member.getRoles().stream()
        .map(memberAuth -> new SimpleGrantedAuthority(memberAuth.getRole())).collect(Collectors.toList()));

        this.member = member;
    }
}
