package com.kn.auth.enums;

public enum ReturnPosition {
    FIRST("ORDER BY id ASC LIMIT 1"),
    LAST("ORDER BY id ASC LIMIT 1"),
    FIRST_SORT("ORDER BY %s ASC LIMIT 1"),
    LAST_SORT("ORDER BY %s DSC LIMIT 1");

    private final String value;

    ReturnPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
