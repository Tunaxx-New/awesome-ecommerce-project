package com.kn.auth.exceptions;

import com.kn.auth.enums.EmailError;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailValidationException extends Exception {
    public EmailValidationException(EmailError emailError) {
        super(emailError.getValue());
    }
}
