package org.example.validation;

import org.example.user.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationEngine {

    private final UserValidationConfig config;

    public ValidationEngine(ObjectMapper objectMapper) {
        try {
            ClassPathResource resource = new ClassPathResource("configs/user-rules.json");

            try (InputStream inputStream = resource.getInputStream()) {
                this.config = objectMapper.readValue(inputStream, UserValidationConfig.class);
            }

        } catch (IOException exception) {
            throw new IllegalStateException("Could not load validation config", exception);
        }
    }

    public List<String> validate(User user) {
        List<String> errors = new ArrayList<>();

        for (ValidationRule rule : config.rules()) {
            String value = getFieldValue(user, rule.field());

            switch (rule.type()) {
                case "not-blank" -> validateNotBlank(value, rule, errors);
                case "regex" -> validateRegex(value, rule, errors);
                case "max-length" -> validateMaxLength(value, rule, errors);
                default -> throw new IllegalArgumentException("Unsupported validation rule type: " + rule.type());
            }
        }

        return errors;
    }

    private String getFieldValue(User user, String field) {
        if (user == null || field == null) {
            return null;
        }

        return switch (field) {
            case "name" -> user.getName();
            case "email" -> user.getEmail();
            case "password" -> user.getPassword();
            default -> throw new IllegalArgumentException("Unsupported validation field: " + field);
        };
    }

    private void validateNotBlank(String value, ValidationRule rule, List<String> errors) {
        if (value == null || value.isBlank()) {
            errors.add(rule.message());
        }
    }

    private void validateRegex(String value, ValidationRule rule, List<String> errors) {
        if (value == null || value.isBlank()) {
            return;
        }

        if (rule.pattern() == null || rule.pattern().isBlank()) {
            throw new IllegalArgumentException("Regex rule is missing pattern for field: " + rule.field());
        }

        if (!value.matches(rule.pattern())) {
            errors.add(rule.message());
        }
    }

    private void validateMaxLength(String value, ValidationRule rule, List<String> errors) {
        if (value == null) {
            return;
        }

        if (rule.maxLength() == null) {
            throw new IllegalArgumentException("Max-length rule is missing maxLength for field: " + rule.field());
        }

        if (value.length() > rule.maxLength()) {
            errors.add(rule.message());
        }
    }
}