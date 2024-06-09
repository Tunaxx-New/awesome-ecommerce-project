package com.kn.auth.controllers;

import java.util.List;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.enums.ErrorMessage;
import com.kn.auth.exceptions.DatabaseIntegrityUniquenessException;
import com.kn.auth.exceptions.EmailValidationException;
import com.kn.auth.exceptions.PasswordValidationException;
import com.kn.auth.models.Authentication;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.requests.AuthenticationRequest;
import com.kn.auth.requests.ListRequest;
import com.kn.auth.responses.AuthenticationResponse;
import com.kn.auth.responses.ErrorResponse;
import com.kn.auth.responses.ProfileResponse;
import com.kn.auth.responses.ResponseWrapper;
import com.kn.auth.services.AuthenticationService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authenticate operations")
public class AuthenticationController {

        private final AuthenticationService service;

        @PostMapping("/private/profile")
        @ResponseBody
        @Operation(summary = "Register new user", description = "Endpoint to register a new user")
        public ResponseEntity<ProfileResponse> profile() {
                ProfileResponse profileResponse = service.findByAspectId(0);
                Authentication authenticationWithUrlImages = service.setImagesUrls(profileResponse.getAuthentication());
                profileResponse.setAuthentication(authenticationWithUrlImages);
                return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
        }

        @PostMapping("/private/profile/changeTransparentPolicies")
        @Operation(summary = "Agree many transparent policies", description = "Gets policie ids to approve them into seller account")
        public ResponseEntity<List<TransparentPolicy>> changeTransparentPolicies(
                        @RequestBody List<TransparentPolicy> transparentPolicies) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(service.changeTransparentPolicies(transparentPolicies, 0));
        }

        @PostMapping("/public/register")
        @ResponseBody
        @Operation(summary = "Register new user", description = "Endpoint to register a new user")
        public ResponseEntity<ResponseWrapper<AuthenticationResponse>> register(
                        @RequestBody AuthenticationRequest request) {
                try {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseWrapper<AuthenticationResponse>(
                                                        new AuthenticationResponse(service.register(request.getEmail(),
                                                                        request.getPassword())),
                                                        null));
                } catch (DatabaseIntegrityUniquenessException e) {
                        if (e.getMessage().contains("Duplicate entry"))
                                return ResponseEntity.status(HttpStatus.CONFLICT)
                                                .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                                ErrorResponse.builder()
                                                                                .error(ErrorMessage.DATABASE_UNIQUE_CONSTRAINT_ISSUE
                                                                                                .getValue())
                                                                                .exceptionMessage(e
                                                                                                .getMostSpecificCause()
                                                                                                .toString())
                                                                                .column(e.getColumn())
                                                                                .build()));
                        else
                                return ResponseEntity.status(HttpStatus.CONFLICT)
                                                .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                                ErrorResponse.builder()
                                                                                .error(ErrorMessage.DATABASE_INTEGRITY_ISSUE
                                                                                                .getValue())
                                                                                .exceptionMessage(e.getMessage())
                                                                                .build()));
                } catch (PasswordValidationException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.WEAK_PASSWORD.getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .column("password")
                                                                        .build()));
                } catch (EmailValidationException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.WEAK_PASSWORD.getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .column("email")
                                                                        .build()));
                } catch (JwtException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.JWT_ISSUE.getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .build()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.UNNAMED_ISSUE.getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .build()));
                }
        }

        @PostMapping("/public/authenticate")
        @Operation(summary = "Login and returns token", description = "Authentication of users their, getting token.")
        public ResponseEntity<ResponseWrapper<AuthenticationResponse>> authenticate(
                        @RequestBody AuthenticationRequest request) {
                try {
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseWrapper<AuthenticationResponse>(
                                                        new AuthenticationResponse(service.authenticate(
                                                                        request.getEmail(), request.getPassword())),
                                                        null));
                } catch (UsernameNotFoundException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.USER_NOT_FOUND.getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .build()));
                } catch (AuthenticationException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(new ResponseWrapper<AuthenticationResponse>(null,
                                                        ErrorResponse.builder()
                                                                        .error(ErrorMessage.AUTHENTICATION_ISSUE
                                                                                        .getValue())
                                                                        .exceptionMessage(e.getMessage())
                                                                        .build()));
                }
        }

}
