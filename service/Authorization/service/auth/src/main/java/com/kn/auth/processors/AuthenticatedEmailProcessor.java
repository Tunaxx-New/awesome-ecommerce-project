package com.kn.auth.processors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.annotation.processing.*;
import com.google.auto.service.AutoService;
import com.kn.auth.annotations.AuthenticatedEmail;

import java.util.Set;

@AutoService(Processor.class)
public class AuthenticatedEmailProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AuthenticatedEmail.class)) {
            if (element.getKind() == ElementKind.PARAMETER) {
                VariableElement parameter = (VariableElement) element;
                if (!parameter.asType().toString().equals("java.lang.String")) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "@AuthenticatedEmail annotation can only be applied to parameters of type String.",
                            parameter);
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // Define the annotations your processor supports
        return Set.of(AuthenticatedEmail.class.getCanonicalName());
    }

}
