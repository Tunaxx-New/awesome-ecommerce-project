package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedEmail;
import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.repositories.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

        private final OrderRepository orderRepository;
        private final OrderItemService orderItemService;
        private final BuyerService buyerService;
        private final CartService cartService;
        private final SellerService sellerService;
        private final AuthenticationService authenticationService;

        public Order create(Order order) {
                return orderRepository.save(order);
        }

        public Page<Order> getAll(Pageable pageable,
                        @AuthenticatedId int authenticationId) {
                Buyer buyer = buyerService.getByAuthenticationId(authenticationId);
                return orderRepository.findAllByBuyerId(buyer.getId(), pageable);
        }

        public Order compose(int orderId) {
                return orderRepository.findById(orderId).get();
        }

        public Order create(@AuthenticatedEmail int authenticationId) {
                Cart cart = cartService.getComposed(authenticationId);
                Order order = Order.builder().buyer(cart.getBuyer()).paymentMethod(cart.getPaymentMethod())
                                .shippingAddress(cart.getShippingAddress()).build();

                if (order.getShippingAddress() == null)
                        throw new RuntimeException("Shipping address is empty");
                if (order.getPaymentMethod() == null)
                        throw new RuntimeException("Payment methods is empty");

                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                for (CartItem cartItem : cart.getCartItems()) {
                        // Check if product not ordered by seller
                        try {
                                Seller seller = sellerService.get(authenticationId);
                                if (seller.getProducts().stream()
                                                .filter(product -> product.getId()
                                                                .equals(cartItem.getProduct().getId()))
                                                .findFirst().orElse(null) != null)
                                        throw new RuntimeException("You are the owner of product: "
                                                        + String.valueOf(cartItem.getProduct().getId()));
                        } catch (NoSuchElementException e) {
                        }

                        List<TransparentPolicy> buyerTransparentPolicies = new ArrayList<>(cart.getBuyer().getAuthentication().getAuthenticationTransparentPolicies());
                        List<TransparentPolicy> sellerTransparentPolicies = new ArrayList<>(cartItem.getProduct().getSeller().getAuthentication().getAuthenticationTransparentPolicies());
                        orderItems.add(OrderItem.builder()
                                        .amount(cartItem.getAmount())
                                        .order(order)
                                        .buyerTransparentPolicies(buyerTransparentPolicies)
                                        .sellerTransparentPolicies(sellerTransparentPolicies)
                                        .commissionPercentage(
                                                        cartItem.getProduct().getSeller().getCommissionPercentage() +
                                                                        cart.getBuyer().getCommissionPercentage())
                                        .product(cartItem.getProduct())
                                        .price(cartItem.getProduct().getPrice())
                                        .build());
                }

                if (orderItems.size() == 0)
                        throw new RuntimeException("Order items is empty");

                Order createdOrder = orderRepository.save(order);
                List<OrderItem> createdOrderItems = orderItemService.createMany(orderItems);
                cartService.clear(authenticationId);
                createdOrder.setOrderItems(createdOrderItems);
                return createdOrder;
        }
}
