package org.example.validation;

import java.util.List;

public record UserValidationConfig(
        String entity,
        List<ValidationRule> rules
) { }
