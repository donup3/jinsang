package com.samplesecurity.repository;

import com.samplesecurity.domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {
}
