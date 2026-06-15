package org.example.pipeline.handler;

import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.example.user.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class SetDefaultsHandler implements PipelineStepHandler {

    @Override
    public String getType() {
        return "SET_DEFAULTS";
    }

    @Override
    public void handle(User user) {
        if (user.getStatus() == null)  {
            user.setStatus(UserStatus.PENDING);
        }
    }
}
