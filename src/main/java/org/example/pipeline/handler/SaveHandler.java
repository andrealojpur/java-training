package org.example.pipeline.handler;

import org.example.pipeline.PipelineException;
import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.example.user.UserService;
import org.example.user.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class SaveHandler implements PipelineStepHandler {

    private final UserService userService;

    public SaveHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getType() {
        return "SAVE";
    }

    @Override
    public void handle(User user) {
        if (user.getStatus() != UserStatus.PENDING) {
            throw new PipelineException("User must be in PENDING status before SAVE step");
        }

        user.setStatus(UserStatus.AVAILABLE);
        userService.save(user);
    }
}
