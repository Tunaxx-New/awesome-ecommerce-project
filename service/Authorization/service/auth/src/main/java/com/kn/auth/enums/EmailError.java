package com.kn.auth.enums;

public enum EmailError {
    WRONG_FORMAT("Wrong format of email, please follow mail@host.domen"),
    NULL("Email object is null ;("),
    EMPTY("Email is empty"),
    CORRECT("Email is correct!");

    private final String value;

    EmailError(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
