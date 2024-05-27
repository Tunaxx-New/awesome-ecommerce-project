package com.kn.auth.services;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.TransactionRequiredException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.AuthenticationToken;
import com.kn.auth.repositories.AuthenticationRepository;
import com.kn.auth.repositories.ResetTokenRepository;
import com.kn.auth.requests.AuthenticationRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessReturnService {
    // TODO: Make forgot password service
    private final AuthenticationRepository authenticationRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void processForgotPassword(AuthenticationRequest request) {
        // Generate a unique reset token
        String resetToken = UUID.randomUUID().toString();
        String email = request.getEmail();
        passwordEncoder.getClass();

        // Create a ResetToken entity and associate it with the user
        AuthenticationToken tokenEntity = new AuthenticationToken();
        tokenEntity.setToken(resetToken);
        // Set expiry date (e.g., LocalDateTime.now().plusHours(1))

        Optional<Authentication> optionalUser = authenticationRepository.findByEmail(email);
        optionalUser.ifPresent(user -> {
            // tokenEntity.setAuthentication(user);
            resetTokenRepository.save(tokenEntity);

            // Send an email to the user with the reset link (containing resetToken)
            // emailService.sendResetPasswordEmail(user.getEmail(), resetToken);
        });
    }

    // @Override
    public void processResetPassword(String token, String newPassword) throws TransactionRequiredException {
        // Find the ResetToken entity by token
        Optional<AuthenticationToken> optionalResetToken = resetTokenRepository.findByToken(token);

        optionalResetToken.ifPresent(resetToken -> {
            // Check if the reset token is not expired
            /*
             * if (resetToken.getExpiryDate().isAfter(a)) {
             * // Update the user's password and delete the reset token
             * Authentication user = resetToken.getAuthentication();
             * user.setPassword(passwordEncoder.encode(newPassword));
             * authenticationRepository.save(user);
             * 
             * resetTokenRepository.delete(resetToken);
             * } else {
             * // Handle expired token
             * throw new TransactionRequiredException("Reset token has expired");
             * }
             */
        });
    }
}
