package com.kn.auth.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.DeadEvent;
import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.enums.TransparentPolicy;
import com.kn.auth.models.Badge;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.Product;
import com.kn.auth.models.ProductReview;
import com.kn.auth.models.Seller;
import com.kn.auth.repositories.BadgeRepository;
import com.kn.auth.repositories.ProductRepository;
import com.kn.auth.repositories.ProductReviewRepository;

import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private final OrderItemService orderItemService;

    private final BadgeRepository badgeRepository;

    public ProductReview review(ProductReview productReview, int productId, @AuthenticatedId int authenticatedId) {
        Buyer buyer = buyerService.getByAuthenticationId(authenticatedId);
        try {
            Seller seller = sellerService.get(authenticatedId);
            if (seller.getProducts().stream().filter(product -> product.getId().equals(productId)).findFirst() != null)
                throw new RuntimeException("You are the owner of product: " + productId);
        } catch (NoSuchElementException e) {

        }

        OrderItem orderItem = buyer.getOrders().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem_ -> orderItem_.getProduct().getId().equals(productId))
                .filter(orderItem_ -> orderItem_.getProductReview() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("You are not ordered products with id: " + productId));
        ProductReview productReviewSafe = new ProductReview();
        productReviewSafe = productReviewSafe.safeUpdate(productReview);
        productReviewSafe.setBuyer(buyer);
        productReviewSafe.setOrderItem(orderItem);
        productReviewSafe.setProduct(orderItem.getProduct());
        ProductReview savedProductReview = productReviewRepository.save(productReviewSafe);
        orderItem.setProductReview(savedProductReview);
        orderItemService.updateMany(List.of(orderItem));
        return productReviewRepository.save(productReviewSafe);
    }

    public List<Order> orders(int productId) {
        return productRepository.findById(productId).get().getOrderItems().stream()
                .map(orderItem -> {
                    Order order = orderItem.getOrder();
                    List<com.kn.auth.enums.TransparentPolicy> sellerTransparentPolicies = orderItem
                            .getSellerTransparentPolicies()
                            .stream()
                            .map(transparentPolicy -> transparentPolicy.getName())
                            .collect(Collectors.toList());
                    List<com.kn.auth.enums.TransparentPolicy> buyerTransparentPolicies = orderItem
                            .getBuyerTransparentPolicies()
                            .stream()
                            .map(transparentPolicy -> transparentPolicy.getName())
                            .collect(Collectors.toList());

                    if (!isTransparentPolicyBetweenAccepted(sellerTransparentPolicies, buyerTransparentPolicies,
                            com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS))
                        return null;

                    if (!isTransparentPolicyBetweenAccepted(sellerTransparentPolicies, buyerTransparentPolicies,
                            com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS_BUYER))
                        order.setBuyer(null);

                    if (!isTransparentPolicyBetweenAccepted(sellerTransparentPolicies, buyerTransparentPolicies,
                            com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS_DATE))
                        order.setCreatedTime(null);

                    if (!isTransparentPolicyBetweenAccepted(sellerTransparentPolicies, buyerTransparentPolicies,
                            com.kn.auth.enums.TransparentPolicy.AVAILABLE_PRODUCT_ORDERS_PRICE))
                        order.setPrice(null);

                    return order;
                })
                .collect(Collectors.toList());
    }

    private boolean isTransparentPolicyBetweenAccepted(
            List<com.kn.auth.enums.TransparentPolicy> first,
            List<com.kn.auth.enums.TransparentPolicy> second,
            com.kn.auth.enums.TransparentPolicy transparentPolicy) {
        return first.contains(transparentPolicy) && second.contains(transparentPolicy);
    }

    public Product create(Product product, @AuthenticatedId int authenticatedId) {
        // Adding first product badge
        if (productRepository.findAllBySellerId(product.getSeller().getId()).get().size() == 0) {
            Buyer buyer = buyerService.getByAuthenticationId(authenticatedId);
            List<Badge> badges = buyer.getBadges();
            Badge badge = badgeRepository.findById(3).get();
            // badge.setDescription(badge.getDescription() + "~" + product.getId());
            badges.add(badge);
            buyer.setBadges(badges);
            buyerService.update(buyer);
        }
        Product product_ = productRepository.findById(product.getId()).get();
        if (product_.getSeller().getAuthentication().getId() != authenticatedId)
            return null;
        if (product.getPrice().compareTo(new BigDecimal(0)) <= -1)
            return null;
        return productRepository.save(product_.safeUpdate(product));
    }

    public Product safeCreate(Product product, @AuthenticatedId int authenticatedId) {
        return productRepository.save(Product.builder().seller(sellerService.get(authenticatedId)).build()
                .safeUpdate(product));
    }

    public Product get(int productId) {
        return productRepository.findById(productId).get();
    }

    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
