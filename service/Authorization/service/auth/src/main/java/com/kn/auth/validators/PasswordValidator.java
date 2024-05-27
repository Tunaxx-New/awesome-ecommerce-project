package com.kn.auth.validators;

import com.kn.auth.enums.PasswordError;

public class PasswordValidator {
    public PasswordError validate(String password) {
        if (password == null)
            return PasswordError.TOO_SHORT;
        if (password == "")
            return PasswordError.EMPTY;
        if (password.length() < 8)
            return PasswordError.TOO_SHORT;
        if (!containsSpecialCharacter(password))
            return PasswordError.MISSING_SPECIAL_CHARACTER;
        if (!containsDigit(password))
            return PasswordError.MISSING_DIGIT;
        if (!containsLowerCase(password))
            return PasswordError.MISSING_LOWERCASE_LETTER;
        if (!containsUpperCase(password))
            return PasswordError.MISSING_UPPERCASE_LETTER;
        return PasswordError.CORRECT;
    }

    private boolean containsSpecialCharacter(String password) {
        // Implement logic to check if the password contains special characters
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    private boolean containsUpperCase(String password) {
        // Implement logic to check if the password contains uppercase letters
        return password.matches(".*[A-Z].*");
    }

    private boolean containsLowerCase(String password) {
        // Implement logic to check if the password contains lowercase letters
        return password.matches(".*[a-z].*");
    }

    private boolean containsDigit(String password) {
        // Implement logic to check if the password contains digits
        return password.matches(".*\\d.*");
    }
}
