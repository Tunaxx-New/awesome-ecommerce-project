package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.AuthenticationToken;

// ResetTokenRepository.java
public interface ResetTokenRepository extends JpaRepository<AuthenticationToken, Long> {
    Optional<AuthenticationToken> findByToken(String token);
}
