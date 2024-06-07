package com.kn.auth.services;

import org.springframework.stereotype.Service;

import com.kn.auth.annotations.AuthenticatedEmail;
import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.repositories.BadgeRepository;
import com.kn.auth.repositories.CartItemRepository;
import com.kn.auth.repositories.OrderItemRepository;
import com.kn.auth.repositories.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
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

        private final BadgeRepository badgeRepository;
        private final OrderItemRepository orderItemRepository;

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

                        List<TransparentPolicy> buyerTransparentPolicies = new ArrayList<>(
                                        cart.getBuyer().getAuthentication().getAuthenticationTransparentPolicies());
                        List<TransparentPolicy> sellerTransparentPolicies = new ArrayList<>(cartItem.getProduct()
                                        .getSeller().getAuthentication().getAuthenticationTransparentPolicies());
                        for (TransparentPolicy policy : buyerTransparentPolicies) {
                                policy.setAuthentications(null);
                                policy.setBuyerOrders(null);
                                policy.setSellerOrders(null);
                        }
                        for (TransparentPolicy policy : sellerTransparentPolicies) {
                                policy.setAuthentications(null);
                                policy.setBuyerOrders(null);
                                policy.setSellerOrders(null);
                        }

                        Integer commissionPercentage = 0;
                        commissionPercentage += BadgeService.calculatePercentage(cart.getBuyer());
                        commissionPercentage += BadgeService.calculatePercentage(cartItem.getProduct().getSeller(),
                                        cart.getBuyer().getBadges());

                        orderItems.add(OrderItem.builder()
                                        .amount(cartItem.getAmount())
                                        .order(order)
                                        .buyerTransparentPolicies(buyerTransparentPolicies)
                                        .sellerTransparentPolicies(sellerTransparentPolicies)
                                        .commissionPercentage(commissionPercentage)
                                        .product(cartItem.getProduct())
                                        .price(cartItem.getProduct().getPrice())
                                        .build());
                }

                if (orderItems.size() == 0)
                        throw new RuntimeException("Order items is empty");

                Order createdOrder = orderRepository.save(order);

                BigDecimal priceSum = new BigDecimal(0);
                for (OrderItem orderItem : orderItems) {
                        Order order_ = orderItem.getOrder();
                        order_.setBuyer(Buyer.builder().id(order_.getBuyer().getId()).build());
                        orderItem.setOrder(order_);
                        if (orderItem.getPrice() != null) {
                                priceSum.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getAmount())));
                        }
                }
                List<OrderItem> createdOrderItems = orderItemService.createMany(orderItems);

                cartService.clear(authenticationId);
                cart.setCartItems(new ArrayList<>());

                // createdOrder.setOrderItems(createdOrderItems);
                // createdOrder.setBuyer(null);

                // Adding first product badge
                // Also can be many, because orders greater than 100$ is something special!
                if (priceSum.compareTo(new BigDecimal(100)) > 0) {
                        Buyer buyer = cart.getBuyer();
                        List<Badge> badges = buyer.getBadges();
                        Badge badge = badgeRepository.findById(5).get();
                        // badge.setDescription(badge.getDescription() + "~" + createdOrder.getId());
                        badges.add(badge);
                        buyer.setBadges(badges);
                        buyerService.update(buyer);
                }

                // Adding first product badge
                // Also can be many, because orders less than 1$ is something special!
                if (priceSum.compareTo(new BigDecimal(1)) < 0) {
                        Buyer buyer = cart.getBuyer();
                        List<Badge> badges = buyer.getBadges();
                        Badge badge = badgeRepository.findById(6).get();
                        // badge.setDescription(badge.getDescription() + "~" + createdOrder.getId());
                        badges.add(badge);
                        buyer.setBadges(badges);

                        buyerService.update(buyer);
                }

                // Adding first product badge
                // Also can be many, because first consumer is smth special!
                for (OrderItem orderItem : createdOrderItems) {
                        if (orderItemRepository.findAllByProductId(orderItem.getProduct().getId()).get().size() == 1) {
                                Buyer buyer = cart.getBuyer();
                                List<Badge> badges = buyer.getBadges();
                                Badge badge = badgeRepository.findById(7).get();
                                // badge.setDescription(badge.getDescription() + "~" + createdOrder.getId() +
                                // "~"
                                // + orderItem.getId());
                                badges.add(badge);
                                buyer.setBadges(badges);
                                buyerService.update(buyer);
                        }
                }

                // Adding first order badge
                if (orderRepository.findAllByBuyerId(cart.getBuyer().getId()).get().size() == 1) {
                        Buyer buyer = cart.getBuyer();
                        List<Badge> badges = buyer.getBadges();
                        Badge badge = badgeRepository.findById(2).get();
                        // badge.setDescription(badge.getDescription() + "~" + createdOrder.getId());
                        badges.add(badge);
                        buyer.setBadges(badges);
                        buyerService.update(buyer);
                }

                return createdOrder;
        }
}
