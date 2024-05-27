package com.kn.auth.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Order;
import com.kn.auth.models.PaymentMethod;
import com.kn.auth.models.ShippingAddress;
import com.kn.auth.requests.SortProperties;

@Transactional
@Rollback
@SpringBootTest
public class SellerServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    ShippingAddressService shippingAddressService;

    List<Integer> orderIds;
    Authentication authentication;
    Buyer buyer;

    @BeforeEach
    void setUp() {
        orderIds = new ArrayList<>();
        authentication = authenticationService.create(Authentication.builder().build());
        buyer = buyerService.create(Buyer.builder().authentication(authentication).build());
        ShippingAddress shippingAddress = shippingAddressService.create(ShippingAddress.builder().build());
        PaymentMethod paymentMethod = paymentMethodService.create(PaymentMethod.builder().build());
        for (int i = 0; i < 10; i++) {
            orderIds.add(orderService.create(Order.builder().buyer(buyer).shippingAddress(shippingAddress)
                    .paymentMethod(paymentMethod).id(i).build()).getId());
        }
    }

    @Test
    void getAll() {
        SortProperties sortProperties = new SortProperties();
        sortProperties.setSortBy("id");
        sortProperties.setSortOrder("Desc");
        Pageable pageable = (Pageable) new org.springdoc.core.converters.models.Pageable(1, 1, List.of("id,desc"));
        List<Order> orders = orderService
                .getAll(pageable, 0)
                .getContent();
        assertEquals(2, orders.size());
        assertEquals(orderIds.get(6), orders.get(1).getId());
        assertEquals(orderIds.get(7), orders.get(0).getId());
    }
}
