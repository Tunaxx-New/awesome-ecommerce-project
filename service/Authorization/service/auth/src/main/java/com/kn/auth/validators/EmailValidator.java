package com.kn.auth.validators;

import com.kn.auth.enums.EmailError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public EmailError validate(String email) {
        if (email == null)
            return EmailError.NULL;
        if (email == "")
            return EmailError.EMPTY;
        
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
            return EmailError.CORRECT;
        else
            return EmailError.WRONG_FORMAT;
    }
}
