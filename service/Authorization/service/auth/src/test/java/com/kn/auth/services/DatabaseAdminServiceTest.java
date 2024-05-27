package com.kn.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.kn.auth.enums.ReturnPosition;
import com.kn.auth.enums.TableName;
import com.kn.auth.models.Authentication;
import com.kn.auth.repositories.AuthenticationRepository;

import jakarta.transaction.Transactional;

@Transactional
@Rollback
@SpringBootTest
public class DatabaseAdminServiceTest {
    @Autowired
    DatabaseAdminService databaseAdminService;
    @Autowired
    AuthenticationRepository authenticationRepository;

    private int testInt = 1;
    private String testString = "string";

    @Test
    void deleteValueByValue() {
        authenticationRepository.save(Authentication.builder().id(testInt).build());
        databaseAdminService.deleteValueByValue(TableName.AUTHENTICATION, "id", String.valueOf(testInt));
    }

    @Test
    void getValueByValue() {
        authenticationRepository.save(Authentication.builder().id(1).email(testString).build());
        String value = databaseAdminService.getValueByValue(TableName.AUTHENTICATION, "email", testString, "id",
                ReturnPosition.FIRST);
        assertEquals(String.valueOf(testInt), value);
    }
}
