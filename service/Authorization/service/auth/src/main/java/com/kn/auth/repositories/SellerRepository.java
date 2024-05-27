package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByAuthenticationId(int authenticationId);
}
