package com.kn.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.kn.auth.exceptions.DatabaseIntegrityUniquenessException;
import com.kn.auth.exceptions.EmailValidationException;
import com.kn.auth.exceptions.PasswordValidationException;
import com.kn.auth.models.Authentication;
import com.kn.auth.models.AuthenticationToken;
import com.kn.auth.models.Buyer;
import com.kn.auth.models.Role;
import com.kn.auth.repositories.AuthenticationTokenRepository;

import io.jsonwebtoken.JwtException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
//@Rollback
@SpringBootTest
public class AuthenticationTest {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    BuyerService buyService;
    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationTokenRepository authenticationTokenRepository;

    String emailTest = "test@gmail.com";
    String passwordTest = "Tunaxx2024!";
    UserDetails user;

    @BeforeEach
    void setUp() {
        user = User.withUsername(emailTest).password(passwordTest).build();
    }

    @Test
    void register()
            throws DatabaseIntegrityUniquenessException,
            JwtException,
            PasswordValidationException,
            EmailValidationException,
            Exception {
        String token = authenticationService.register(emailTest, passwordTest);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void create() {
        Authentication authentication = Authentication.builder().email(emailTest).password(passwordTest).build();
        Authentication createdAuthentication = authenticationService.create(authentication);
        assertEquals(authentication.getEmail(), createdAuthentication.getEmail());
    }

    @Test
    @DependsOn({ "create", "findByEmail" })
    void deleteByEmail() {
        Authentication authentication = Authentication.builder()
                .email(emailTest)
                .password(passwordTest)
                .roles(List.of(Role.builder().name(com.kn.auth.enums.Role.USER).build()))
                .build();
        authenticationService.create(authentication);
        authenticationTokenRepository.save(AuthenticationToken.builder().token(emailTest).authentication(authentication).build());
        buyService.create(Buyer.builder().authentication(authentication).build());
        authenticationService.deleteByEmail(emailTest);
        assertThrowsExactly(NoSuchElementException.class, () -> authenticationService.findByEmail(emailTest));
    }

    @Test
    void findByEmail() {
        Authentication authentication = Authentication.builder().email(emailTest).password(passwordTest).build();
        authenticationService.create(authentication);
        assertEquals(authenticationService.findByEmail(emailTest).getEmail(), emailTest);
    }

}