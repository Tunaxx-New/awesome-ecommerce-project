package com.kn.auth.runners;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import com.kn.auth.annotations.CsvTest;

public class CsvTestRunner {
    public static void runTests(Object testInstance)
            throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = testInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(CsvTest.class)) {
                CsvTest csvTest = method.getAnnotation(CsvTest.class);
                String inputFile = csvTest.inputFile();
                String outputFile = csvTest.outputFile();

                String[] inputs = readCSV(inputFile);
                String outputs = outputFile;

                method.invoke(testInstance, (Object) inputs, (Object) outputs);
            }
        }
    }

    private static String[] readCSV(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        System.out.println(filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            //String line;
            //while ((line = reader.readLine()) != null) {
            //    lines.add(line);
            //}
        }
        return lines.toArray(new String[0]);
    }
}
