package com.samplesecurity.repository;

import com.samplesecurity.controller.BoardController;
import com.samplesecurity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);

    Optional<Member> findByNickName(String nickName);
}
