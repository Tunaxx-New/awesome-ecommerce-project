package com.kn.auth.enums;

public enum Role {
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR"),
    USER("USER"),
    BUYER("BUYER"),
    SELLER("SELLER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
