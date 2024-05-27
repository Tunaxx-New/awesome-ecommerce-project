package com.kn.auth.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {
}
