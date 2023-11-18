package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.Authentication;

public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    Optional<Authentication> findByEmail(String email);
}
