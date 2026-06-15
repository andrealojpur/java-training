package org.example.pipeline;

import org.example.user.User;

public interface PipelineStepHandler {
    String getType();
    void handle(User user);
}
