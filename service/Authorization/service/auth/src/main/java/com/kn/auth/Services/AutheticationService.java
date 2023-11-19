package com.kn.auth.services;

import java.util.function.Supplier;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kn.auth.enums.Role;
import com.kn.auth.models.Authentication;
import com.kn.auth.repositories.AuthenticationRepository;
import com.kn.auth.requests.AuthenticationRequest;
import com.kn.auth.responses.AuthenticationResponse;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutheticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationRepository repository;

    // TODO: replace with Register entities and classes
    public ResponseEntity<AuthenticationResponse> register(AuthenticationRequest request) {
        try {
            Authentication authentication = Authentication.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            repository.save(authentication);

            String jwtToken = jwtService.generateToken(authentication);
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("_authorization.unique_email")) {
                AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                        .error("Email already exists")
                        .exceptionMessage(e.getMessage())
                        .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            } else {

                AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                        .error("Data integrity violation")
                        .exceptionMessage(e.getMessage())
                        .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
        } catch (JwtException e) {
            AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                    .error("Error generating JWT token")
                    .exceptionMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                    .error("An error occurred")
                    .exceptionMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
            Authentication authentication = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            var jwtToken = jwtService.generateToken(authentication);
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
        } catch (UsernameNotFoundException e) {
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .error("User not found")
                    .exceptionMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authenticationResponse);
        } catch (AuthenticationException e) {
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .error("Authentication failed")
                    .exceptionMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }
    }

}
