package com.kn.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Buyer;

@Transactional
@Rollback
@SpringBootTest
public class BuyerServiceTest {
    @Autowired
    BuyerService buyerService;
    @Autowired
    AuthenticationService authenticationService;
    String testString = "TEST";
    int testInt = 1;
    Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = authenticationService.create(Authentication.builder().build());
    }

    @Test
    void create() {
        Buyer createdBuyer = buyerService
                .create(Buyer.builder().id(testInt).authentication(authentication).name(testString).build());
        Buyer foundBuyer = buyerService.getById(createdBuyer.getId());
        assertEquals(foundBuyer.getName(), testString);
    }

    @Test
    @DependsOn("create")
    void getById() {
        Buyer createdBuyer = buyerService
                .create(Buyer.builder().id(testInt).authentication(authentication).build());
        Buyer foundBuyer = buyerService.getById(createdBuyer.getId());
        assertEquals(foundBuyer.getId(), createdBuyer.getId());
    }

    @Test
    @DependsOn("create")
    void getByAuthenticationId() {
        Buyer createdBuyer = buyerService
                .create(Buyer.builder().id(testInt).authentication(authentication).build());
        Buyer foundBuyer = buyerService.getById(createdBuyer.getId());
        assertEquals(foundBuyer.getAuthentication().getId(), authentication.getId());
    }

    @Test
    @DependsOn("create")
    void delete() {
        Buyer buyer = buyerService
                .create(Buyer.builder().id(testInt).authentication(authentication).name(testString).build());
        int id = buyer.getId();
        buyerService.delete(buyer);
        try {
            buyerService.getById(id);
            fail("Expected NoSuchElementException was not thrown");
        } catch (java.util.NoSuchElementException e) {
        }
    }
}