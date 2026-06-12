package org.example.validation;

public record ValidationRule(
        String field,
        String type,
        String pattern,
        Integer maxLength,
        String message
) { }
