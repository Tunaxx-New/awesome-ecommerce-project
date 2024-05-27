package com.kn.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.kn.auth.annotations.CsvTest;
import com.kn.auth.runners.CsvTestParameterResolver;
import com.kn.auth.runners.CsvTestRunner;

@SpringBootTest
@ExtendWith(CsvTestParameterResolver.class)
public class DatabaseServiceTest {
    @Autowired
    DatabaseService databaseService;

    @Test
    @CsvTest(inputFile = "input.csv", outputFile = "output.csv")
    void isColumnByConstraintRight(String[] inputs, String output_filename)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        System.out.println(output_filename + " " + inputs);
        assertEquals("output.csv", output_filename);
        CsvTestRunner.runTests(this);
        // databaseService.contstraintColumn("UK_8s36ywgnws4k1ij2n3tj6l2dm")
    }
}
