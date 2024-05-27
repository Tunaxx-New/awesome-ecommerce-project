package com.kn.auth.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Buyer;

public class SafeReadUpdateTest {
    int testInt = 1;
    int testIntUpdated = 2;
    String testString = "TEST";
    String testStringUpdated = "TEST_UPDATED";

    @Test
    void isSensitiveDataHides() {
        Buyer buyer = Buyer.builder().id(testInt).authentication(Authentication.builder().id(testInt).build()).build();
        assertEquals(buyer.safeReturn().getAuthentication(), null);
        assertEquals(buyer.safeReturn().getId(), testInt);
    }

    @Test
    void isSafeUpdated() {
        Buyer buyer = Buyer.builder().id(testInt).name(testString).build();
        Buyer updatedBuyer = Buyer.builder().id(testIntUpdated).name(testStringUpdated).build();
        buyer = buyer.safeUpdate(updatedBuyer);
        assertEquals(buyer.getId(), testInt);
        assertEquals(buyer.getName(), testStringUpdated);
    }
}
