package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;
import com.kn.auth.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(com.kn.auth.enums.Role name);
}
