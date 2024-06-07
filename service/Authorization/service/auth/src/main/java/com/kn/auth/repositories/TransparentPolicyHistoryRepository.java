package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kn.auth.models.TransparentPolicyHistory;

import jakarta.transaction.Transactional;

public interface TransparentPolicyHistoryRepository extends JpaRepository<TransparentPolicyHistory, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM TransparentPolicyHistory p WHERE p.authentication.id = :authenticationId")
    void deleteAllByAuthenticationId(@Param("authenticationId") int authenticationId);
}
