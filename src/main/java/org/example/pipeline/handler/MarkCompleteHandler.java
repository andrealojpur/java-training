package org.example.pipeline.handler;

import org.example.pipeline.PipelineException;
import org.example.pipeline.PipelineStepHandler;
import org.example.user.User;
import org.example.user.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class MarkCompleteHandler implements PipelineStepHandler {

    @Override
    public String getType() {
        return "MARK_COMPLETE";
    }

    @Override
    public void handle(User user) {
        if (user.getStatus() != UserStatus.AVAILABLE) {
            throw new PipelineException("Only AVAILABLE users can be marked as COMPLETED");
        }

        user.setStatus(UserStatus.COMPLETED);
    }
}
