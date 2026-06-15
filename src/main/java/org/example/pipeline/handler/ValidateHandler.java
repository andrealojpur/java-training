package org.example.pipeline.handler;

import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.example.validation.ValidationEngine;
import org.example.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidateHandler implements PipelineStepHandler {

    private final ValidationEngine validationEngine;

    public ValidateHandler(ValidationEngine validationEngine) {
        this.validationEngine = validationEngine;
    }

    @Override
    public String getType() {
        return "VALIDATE";
    }

    @Override
    public void handle(User user) {
        List<String> errors = validationEngine.validate(user);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
