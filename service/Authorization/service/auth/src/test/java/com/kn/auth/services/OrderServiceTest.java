package com.kn.auth.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Arrays;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;
import com.kn.auth.models.PaymentMethod;
import com.kn.auth.models.ShippingAddress;

@Transactional
@Rollback
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    ShippingAddressService shippingAddressService;
    @Autowired
    SellerService sellerService;

    Authentication authentication;

    String testString = "key";
    Order order;

    @BeforeEach
    void setUp() {
        authentication = authenticationService.create(Authentication.builder().build());
        PaymentMethod paymentMethod = PaymentMethod.builder().build();
        ShippingAddress shippingAddress = ShippingAddress.builder().build();
        Buyer buyer = Buyer.builder().authentication(authentication).name(testString).build();
        order = orderService.create(
                Order.builder().shippingAddress(shippingAddress).paymentMethod(paymentMethod).buyer(buyer).build());
    }

    @Test
    void compose() {
        Order order_ = orderService.compose(order.getId());
        List<OrderItem> orderItems = Arrays.asList(
                OrderItem.builder().order(order).build(),
                OrderItem.builder().order(order).build(),
                OrderItem.builder().order(order).build());
        assertEquals(order_.getBuyer().getName(), testString);
        assertEquals(order_.getOrderItems().size(), orderItems.size());
        assertEquals(order_.getOrderItems().get(0).getOrder().getBuyer().getName(),
                order.getBuyer().getName());
    }
}
