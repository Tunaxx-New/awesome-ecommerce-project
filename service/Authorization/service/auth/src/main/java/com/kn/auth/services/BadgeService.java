package com.kn.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.models.TransparentPolicyHistory;
import com.kn.auth.repositories.BadgeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public Badge test() {
        return badgeRepository.findAll().get(0);
    }

    public List<Badge> getAll() {
        return badgeRepository.findAll();
    }

    public static Integer calculatePercentage(Buyer buyer, Integer currentCommission) {
        // Badge decrease commision
        Double commissionPercentage = currentCommission + 0.0;
        for (Badge badge : buyer.getBadges()) {
            switch (badge.getId()) {
                case 5:
                    commissionPercentage -= 0.25 * commissionPercentage;
                    break;
                case 6:
                    commissionPercentage -= 0.2 * commissionPercentage;
                    break;
                case 2:
                    commissionPercentage -= 0.1 * commissionPercentage;
                    break;
            }
        }
        return commissionPercentage.intValue();
    }

    public static Integer calculatePercentage(Integer currentCommission,
            List<TransparentPolicyHistory> transparentPolicyHistories) {
        // Transparent policy decrease commision
        Double commissionPercentage = currentCommission + 0.0;
        for (TransparentPolicyHistory transparentPolicyHistory : transparentPolicyHistories) {
            switch (transparentPolicyHistory.getTransparentPolicy().getId()) {
                case 1:
                    commissionPercentage -= 0.25 * commissionPercentage;
                    break;
                case 2:
                    commissionPercentage -= 0.05 * commissionPercentage;
                    break;
                case 3:
                    commissionPercentage -= 0.05 * commissionPercentage;
                    break;
                case 4:
                    commissionPercentage -= 0.3 * commissionPercentage;
                    break;
            }
        }
        return commissionPercentage.intValue();
    }

    public static Integer calculatePercentage(Integer currentCommission,
            TransparentPolicyHistory transparentPolicyHistory) {
        // Transparent policy decrease commision
        Double commissionPercentage = currentCommission + 0.0;
        switch (transparentPolicyHistory.getTransparentPolicy().getId()) {
            case 1:
                commissionPercentage -= 0.25 * commissionPercentage;
                break;
            case 2:
                commissionPercentage -= 0.05 * commissionPercentage;
                break;
            case 3:
                commissionPercentage -= 0.05 * commissionPercentage;
                break;
            case 4:
                commissionPercentage -= 0.3 * commissionPercentage;
                break;
        }
        return commissionPercentage.intValue();
    }

    public static Integer calculatePercentage(Seller seller, Integer currentCommission, List<Badge> badges) {
        // Badge decrease commision
        Double commissionPercentage = currentCommission + 0.0;
        for (Badge badge : badges) {
            switch (badge.getId()) {
                case 3:
                    commissionPercentage -= 0.25 * commissionPercentage;
                    break;
            }
        }
        return commissionPercentage.intValue();
    }
}
