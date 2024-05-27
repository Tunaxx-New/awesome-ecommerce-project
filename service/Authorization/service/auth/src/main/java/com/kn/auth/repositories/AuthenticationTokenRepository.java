package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.AuthenticationToken;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Integer> {
}
