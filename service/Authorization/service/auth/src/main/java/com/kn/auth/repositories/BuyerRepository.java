package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kn.auth.models.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    Optional<Buyer> findByAuthenticationId(int authenticationId);
}
