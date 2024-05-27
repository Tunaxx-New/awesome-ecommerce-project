package com.kn.auth.enums;

public enum TransparentPolicy {
    AVAILABLE_PRODUCT_ORDERS("AVAILABLE_PRODUCT_ORDERS"),
    AVAILABLE_PRODUCT_ORDERS_DATE("AVAILABLE_PRODUCT_ORDERS_DATE"),
    AVAILABLE_PRODUCT_ORDERS_PRICE("AVAILABLE_PRODUCT_ORDERS_PRICE"),
    AVAILABLE_PRODUCT_ORDERS_BUYER("AVAILABLE_PRODUCT_ORDERS_BUYER");

    private final String value;

    TransparentPolicy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
