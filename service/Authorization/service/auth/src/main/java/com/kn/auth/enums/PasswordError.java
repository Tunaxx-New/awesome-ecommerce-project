package com.kn.auth.enums;

public enum PasswordError {
    TOO_SHORT("Password too short"),
    MISSING_SPECIAL_CHARACTER("Missing special character"),
    MISSING_UPPERCASE_LETTER("Missing uppercase letter"),
    MISSING_LOWERCASE_LETTER("Missing lowercase letter"),
    MISSING_DIGIT("Missing digit"),
    NULL("Password object is null ;("),
    EMPTY("Password is empty"),
    CORRECT("Password is correct!");

    private final String value;

    PasswordError(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
