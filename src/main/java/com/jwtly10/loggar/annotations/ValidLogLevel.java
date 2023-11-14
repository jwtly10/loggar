package com.jwtly10.loggar.annotations;

import com.jwtly10.loggar.validators.ValidLogLevelValidator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLogLevelValidator.class)
public @interface ValidLogLevel {
    String message() default "Invalid log level";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
