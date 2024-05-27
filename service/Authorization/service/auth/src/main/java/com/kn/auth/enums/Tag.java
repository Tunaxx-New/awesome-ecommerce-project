package com.kn.auth.enums;

public enum Tag {
    NEW("NEW"),
    POPULAR("POPULAR"),
    SALE("SALE"),
    LIMITED("LIMITED");

    private final String value;

    Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
