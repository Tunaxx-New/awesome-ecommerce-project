package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
import com.kn.auth.models.Product;
import com.kn.auth.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final BuyerService buyerService;

    public Cart getComposed(@AuthenticatedId int authenticationId) {
        return buyerService.getByAuthenticationId(authenticationId).getCart();
        // return cartRepository.findByAuthenticationId(authenticationId).get();
        /*
         * Cart cart = cartRepository.findByAuthenticationId(authenticationId).get();
         * return CartResponse.builder()
         * .cart(cart)
         * .cartItems(cartItemService.getAllByAuthenticationId(authenticationId))
         * .build();
         */
    }

    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart update(Cart updatedCart, @AuthenticatedId int authenticationId) {
        Cart cart = cartRepository.findByAuthenticationId(authenticationId).get();
        return cartRepository.save(cart.safeUpdate(updatedCart));
    }

    public CartItem add(int productId, @AuthenticatedId int authenticationId) {
        Cart cart = cartRepository.findByAuthenticationId(authenticationId).get();
        CartItem cartItem = CartItem.builder()
                .product(productService.get(productId))
                .cart(cart)
                .amount(1)
                .build();
        CartItem sameProductCartItem = cartItemService.getAllByCartId(cart.getId()).stream()
                .filter(cartItem_ -> cartItem_.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
        if (sameProductCartItem != null) {
            sameProductCartItem.setAmount(cartItem.getAmount() + sameProductCartItem.getAmount());
            return cartItemService.create(sameProductCartItem);
        }

        return cartItemService.create(cartItem);
    }

    public List<CartItem> changeItems(List<CartItem> cartItems, @AuthenticatedId int authenticationId) {
        Cart cart = cartRepository.findByAuthenticationId(authenticationId).get();
        List<CartItem> actualCartItems = cartItemService.getAllByCartId(cart.getId());

        List<Integer> productIds = new ArrayList<>();
        List<CartItem> uniqueCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            // Skip unauthorized cart items
            for (CartItem actualCartItem : actualCartItems) {
                if (!actualCartItem.getId().equals(cartItem.getId()))
                    continue;

                if (cartItem.getAmount() < 0)
                    cartItem.setAmount(0);

                // Collapse items with same product items
                if (productIds.contains(actualCartItem.getProduct().getId())) {
                    for (CartItem uniqueCartItem : uniqueCartItems)
                        if (uniqueCartItem.getProduct().getId().equals(actualCartItem.getProduct().getId()))
                            uniqueCartItem.setAmount(uniqueCartItem.getAmount() + cartItem.getAmount());
                } else {
                    /*
                    uniqueCartItems.add(CartItem.builder()
                            .id(cartItem.getId())
                            .amount(cartItem.getAmount())
                            .cart(cart)
                            .product(Product.builder().id(cartItem.getProduct().getId()).build())
                            .build());
                    */
                    uniqueCartItems.add(actualCartItem);
                }
            }
        }
        //cartItemService.deleteAllByCartId(cart.getId());
        return cartItemService.updateMany(uniqueCartItems);
    }

    public Boolean clear(@AuthenticatedId int authenticationId) {
        Cart cart = cartRepository.findByAuthenticationId(authenticationId).get();
        cartItemService.deleteAllByCartId(cart.getId());
        return true;
    }
}