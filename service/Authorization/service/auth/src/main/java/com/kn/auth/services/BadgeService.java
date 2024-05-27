package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.models.Badge;
import com.kn.auth.repositories.BadgeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    public Badge test() {
        return badgeRepository.findAll().get(0);
    }
}
