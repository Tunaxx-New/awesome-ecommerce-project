package com.kn.auth.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.ResetToken;
import com.kn.auth.repositories.AuthenticationRepository;
import com.kn.auth.repositories.ResetTokenRepository;
import com.kn.auth.requests.AuthenticationRequest;

import jakarta.persistence.TransactionRequiredException;
import lombok.RequiredArgsConstructor;

// ForgotPasswordServiceImpl.java
@Service
@RequiredArgsConstructor
public class ForgotService {

    // Autowire necessary repositories and services
    private final AuthenticationRepository authenticationRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    //@Override
    public void processForgotPassword(AuthenticationRequest request) {
        // Generate a unique reset token
        String resetToken = UUID.randomUUID().toString();
        String email = request.getEmail();

        // Create a ResetToken entity and associate it with the user
        ResetToken tokenEntity = new ResetToken();
        tokenEntity.setToken(resetToken);
        // Set expiry date (e.g., LocalDateTime.now().plusHours(1))

        Optional<Authentication> optionalUser = authenticationRepository.findByEmail(email);
        optionalUser.ifPresent(user -> {
            tokenEntity.setAuthentication(user);
            resetTokenRepository.save(tokenEntity);

            // Send an email to the user with the reset link (containing resetToken)
            //emailService.sendResetPasswordEmail(user.getEmail(), resetToken);
        });
    }

    //@Override
    public void processResetPassword(String token, String newPassword) {
        // Find the ResetToken entity by token
        Optional<ResetToken> optionalResetToken = resetTokenRepository.findByToken(token);

        optionalResetToken.ifPresent(resetToken -> {
            // Check if the reset token is not expired
            if (resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                // Update the user's password and delete the reset token
                Authentication user = resetToken.getAuthentication();
                user.setPassword(passwordEncoder.encode(newPassword));
                authenticationRepository.save(user);

                resetTokenRepository.delete(resetToken);
            } else {
                // Handle expired token
                throw new TransactionRequiredException("Reset token has expired");
            }
        });
    }
}
