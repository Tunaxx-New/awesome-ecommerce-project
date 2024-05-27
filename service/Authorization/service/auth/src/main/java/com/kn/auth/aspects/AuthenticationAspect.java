package com.kn.auth.aspects;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

import com.kn.auth.annotations.AuthenticatedEmail;
import com.kn.auth.annotations.AuthenticatedId;
import com.kn.auth.services.AuthenticationService;

import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor
public class AuthenticationAspect {

    @Autowired
    private final AuthenticationService authenticationService;

    @Around("execution(public * *(.., @com.kn.auth.annotations.AuthenticatedEmail (*), ..))")
    public Object getEmailFromAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return joinPoint.proceed();
        String email = authentication.getName();
        return setArgsByAnnotationClass(email, null, AuthenticatedEmail.class, joinPoint);
    }

    @Around("execution(public * *(.., @com.kn.auth.annotations.AuthenticatedId (*), ..))")
    public Object getIdFromAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return joinPoint.proceed();
        String email = authentication.getName();
        int id = authenticationService.findByEmail(email).getId();
        return setArgsByAnnotationClass(id, 0, AuthenticatedId.class, joinPoint);
    }

    private <T> Object setArgsByAnnotationClass(
            T value,
            T defaultValue,
            Class<? extends Annotation> annotationClassToCheck,
            ProceedingJoinPoint joinPoint) throws Throwable, NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Annotation[][] annotations_all = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes)
                .getParameterAnnotations();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Annotation[] annotations = annotations_all[i];
            boolean containsAnnotation = Arrays.asList(annotations)
                    .stream()
                    .anyMatch(annotation -> annotation.annotationType().equals(annotationClassToCheck));
            if (args[i] != null && containsAnnotation && args[i].equals(defaultValue)) {
                args[i] = value;
            }
        }
        return joinPoint.proceed(args);
    }
}
