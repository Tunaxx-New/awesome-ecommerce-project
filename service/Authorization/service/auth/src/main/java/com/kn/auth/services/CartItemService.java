package com.kn.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kn.auth.annotations.AuthenticatedEmail;
import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.CartItem;
import com.kn.auth.repositories.BadgeRepository;
import com.kn.auth.repositories.CartItemRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final BuyerService buyerService;
    private final BadgeRepository badgeRepository;

    /**
     * @param shippingAddress
     * @return CartItem
     */
    public CartItem create(CartItem cartItem) {

        // Adding first product badge
        if (cartItemRepository.findAllByCartId(cartItem.getCart().getId()).get().size() == 0) {
            Buyer buyer = buyerService.getByAuthenticationId(cartItem.getCart().getBuyer().getAuthentication().getId());
            List<Badge> badges = buyer.getBadges();
            if (badges.stream().allMatch(badge -> {
                return !badge.getId().equals(4);
            })) {
                badges.add(badgeRepository.findById(4).get());
                buyer.setBadges(badges);
                buyerService.update(buyer);
            }
        }

        return cartItemRepository.save(cartItem);
    }

    /**
     * @param entities
     * @return List<CartItem>
     */
    public List<CartItem> createMany(List<CartItem> entities) {
        return cartItemRepository.saveAll(entities);
    }

    /**
     * @param cartId
     * @return List<CartItem>
     */
    public List<CartItem> getAllByCartId(int cartId) {
        return cartItemRepository.findAllByCartId(cartId).get();
    }

    /**
     * @param authentication_id
     * @return List<CartItem>
     */
    public List<CartItem> getAllByAuthenticationId(@AuthenticatedEmail int authentication_id) {
        return cartItemRepository.findAllByAuthenticationId(authentication_id).get();
    }

    /**
     * @param authentication_id
     * @return List<CartItem>
     */
    public CartItem getByProducId(int productId) {
        return cartItemRepository.findByProductId(productId).orElse(null);
    }

    /**
     * @param cartItems
     * @return List<CartItem>
     */
    public List<CartItem> updateMany(List<CartItem> cartItems) {
        return cartItemRepository.saveAll(cartItems);
    }

    /**
     * @param cartItems
     * @return List<CartItem>
     */
    public List<CartItem> updateManySafe(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems)
            cartItem = new CartItem().safeUpdate(cartItem);
        return cartItemRepository.saveAll(cartItems);
    }

    /**
     * @param cartId
     */
    @Transactional
    public void deleteAllByCartId(int cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }
}
