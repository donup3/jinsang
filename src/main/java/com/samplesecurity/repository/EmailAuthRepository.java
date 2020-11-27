package com.samplesecurity.repository;

import com.samplesecurity.domain.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByEmail(String email);

    Optional<EmailAuth> findByAuthCode(String authCode);
}
