package com.samplesecurity.service;

import com.samplesecurity.domain.EmailAuth;
import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.Role;
import com.samplesecurity.dto.MemberDto;
import com.samplesecurity.repository.EmailAuthRepository;
import com.samplesecurity.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;

    public boolean nickNameChecker(String nickName) {
        Optional<Member> member = memberRepository.findByNickName(nickName);
        if (member.isPresent()){
            return true;
        }
        return false;
    }
}
