package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.models.PaymentMethod;
import com.kn.auth.repositories.PaymentMethodRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod create(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }
}
