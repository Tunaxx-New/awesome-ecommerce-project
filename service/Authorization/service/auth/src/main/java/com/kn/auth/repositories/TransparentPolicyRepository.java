package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.TransparentPolicy;

public interface TransparentPolicyRepository extends JpaRepository<TransparentPolicy, Integer> {
}
