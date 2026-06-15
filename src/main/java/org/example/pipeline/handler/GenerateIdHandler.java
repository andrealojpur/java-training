package org.example.pipeline.handler;

import org.example.pipeline.PipelineException;
import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.example.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class GenerateIdHandler implements PipelineStepHandler {

    private final UserService userService;

    public GenerateIdHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getType() {
        return "GENERATE_ID";
    }

    @Override
    public void handle(User user) {
        if (user.getId() == null) {
            user.setId(userService.getNextId());
            return;
        }

        if (userService.getUserById(user.getId()).isPresent()) {
            throw new PipelineException("User with id " + user.getId() + " already exists");
        }
    }

}
