package com.kn.auth.runners;

import org.junit.jupiter.api.extension.*;

import com.kn.auth.annotations.CsvTest;

import java.lang.reflect.Parameter;

public class CsvTestParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(CsvTest.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        if (parameter.isAnnotationPresent(CsvTest.class)) {
            CsvTest csvTestAnnotation = parameter.getAnnotation(CsvTest.class);
            return new String[] {csvTestAnnotation.inputFile(), csvTestAnnotation.outputFile()};
        }
        throw new ParameterResolutionException("Parameter is not annotated with @CsvTest");
    }
}
