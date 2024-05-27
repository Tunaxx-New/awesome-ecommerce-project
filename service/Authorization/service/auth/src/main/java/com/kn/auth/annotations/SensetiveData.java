package com.kn.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonIgnore
public @interface SensetiveData {
}
