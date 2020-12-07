package com.samplesecurity.security;

import com.samplesecurity.domain.Member;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.security.domain.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NullPointerException("member를 찾지 못했습니다."));

        log.info("member" + member);
        return new CustomUser(member);
    }
}
