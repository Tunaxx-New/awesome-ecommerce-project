package com.kn.auth.services;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.enums.Role;
import com.kn.auth.models.Authentication;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.Product;
import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.repositories.AuthenticationRepository;
import com.kn.auth.repositories.RoleRepository;
import com.kn.auth.repositories.SellerRepository;
import com.kn.auth.repositories.TransparentPolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final AuthenticationRepository authenticationRepository;
    private final RoleRepository roleRepository;
    private final BuyerService buyerService;

    public Seller create(Seller seller, @AuthenticatedId int authenticationId) {
        Authentication authentication = authenticationRepository.findById(authenticationId).get();
        Seller seller_ = Seller.builder().authentication(authentication).build();

        authentication.getRoles().add(roleRepository.findByName(Role.SELLER).get());
        authenticationRepository.save(authentication);

        return sellerRepository.save(seller_.safeUpdate(seller));
    }

    public Seller get(@AuthenticatedId int authenticationId) {
        return sellerRepository.findByAuthenticationId(authenticationId).get();
    }

    public Seller update(Seller updatedSeller, @AuthenticatedId int authenticationId) {
        Seller seller = sellerRepository.findByAuthenticationId(authenticationId).get();
        return sellerRepository.save(seller.safeUpdate(updatedSeller));
    }

    /*
     * LoyaltyIndex =
     * i each
     * Transparency providing sum buyer and seller for each ordered Item
     * if match buyer and seller transparent policy => +value of policy
     * <*>
     * Buyer loyalty indexes
     * divided by
     */
    public BigDecimal getLoyaltyIndex(@AuthenticatedId int authenticationId) {
        Seller seller = get(authenticationId);
        BigDecimal loyaltyIndex = new BigDecimal(0);
        int i = 0;
        for (Product product : seller.getProducts()) {
            for (OrderItem orderItem : product.getOrderItems()) {
                i++;
                List<TransparentPolicy> sellerTransparentPolicies = orderItem.getSellerTransparentPolicies();
                List<TransparentPolicy> buyerTransparentPolicies = orderItem.getBuyerTransparentPolicies();
                for (TransparentPolicy sellerTransparentPolicy : sellerTransparentPolicies)
                    if (buyerTransparentPolicies.contains(sellerTransparentPolicy))
                        loyaltyIndex.add(new BigDecimal(sellerTransparentPolicy.getValue()));
                loyaltyIndex.add(buyerService.getLoyaltyIndex(orderItem.getOrder().getBuyer()));
            }
        }
        return loyaltyIndex.divide(new BigDecimal(i), 10, RoundingMode.HALF_UP);
    }
}
