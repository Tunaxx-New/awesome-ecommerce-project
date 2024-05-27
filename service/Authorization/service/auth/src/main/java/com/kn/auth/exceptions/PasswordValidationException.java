package com.kn.auth.exceptions;

import com.kn.auth.enums.PasswordError;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PasswordValidationException extends Exception {
    public PasswordValidationException(PasswordError passwordError) {
        super(passwordError.getValue());
    }
}
