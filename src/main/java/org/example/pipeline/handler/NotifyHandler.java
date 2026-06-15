package org.example.pipeline.handler;

import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.springframework.stereotype.Service;

@Service
public class NotifyHandler implements PipelineStepHandler {

    @Override
    public String getType() {
        return "NOTIFY";
    }

    @Override
    public void handle(User user) {
        System.out.println("Notification sent for user with id: " + user.getId());
    }
}
