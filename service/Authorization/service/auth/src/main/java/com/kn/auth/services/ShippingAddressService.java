package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.models.ShippingAddress;
import com.kn.auth.repositories.ShippingAddressRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;

    public ShippingAddress create(ShippingAddress shippingAddress) {
        return shippingAddressRepository.save(shippingAddress);
    }

    public List<ShippingAddress> getAllShippingAddress() {
        return shippingAddressRepository.findAll();
    }
}
