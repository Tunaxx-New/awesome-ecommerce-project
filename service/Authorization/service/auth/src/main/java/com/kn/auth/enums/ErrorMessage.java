package com.kn.auth.enums;

public enum ErrorMessage {
    DATABASE_UNIQUE_CONSTRAINT_ISSUE("Unique constraint database error"),
    DATABASE_INTEGRITY_ISSUE("Data integrity violation"),
    JWT_ISSUE("Error generating JWT token"),
    UNNAMED_ISSUE("An error occurred"),
    USER_NOT_FOUND("User not found"),
    AUTHENTICATION_ISSUE("Authentication failed"),
    WEAK_PASSWORD("Password validation error"),
    WEAK_EMAIL("Email validation error");

    private final String value;

    ErrorMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
