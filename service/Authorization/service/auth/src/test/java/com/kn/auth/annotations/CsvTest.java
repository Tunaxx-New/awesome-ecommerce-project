package com.kn.auth.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CsvTest {
    String inputFile();

    String outputFile();
}