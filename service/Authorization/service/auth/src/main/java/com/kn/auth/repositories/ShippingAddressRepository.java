package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.ShippingAddress;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {
}
