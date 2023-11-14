package com.jwtly10.loggar.validators;

import com.jwtly10.loggar.annotations.ValidLogLevel;
import com.jwtly10.loggar.model.LogEntry;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValidLogLevelValidator implements ConstraintValidator<ValidLogLevel, String> {

    @Override
    public void initialize(ValidLogLevel constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(LogEntry.LogLevel.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(value));
    }
}
