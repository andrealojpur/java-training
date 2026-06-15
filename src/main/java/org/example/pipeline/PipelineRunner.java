package org.example.pipeline;

import org.example.user.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PipelineRunner {

    private final ObjectMapper objectMapper;
    private final Map<String, PipelineStepHandler> handlers;

    public PipelineRunner(ObjectMapper objectMapper, List<PipelineStepHandler> handlers) {
        this.objectMapper = objectMapper;
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(PipelineStepHandler::getType, Function.identity()));
    }

    public User runInitialPipeline(User user) {
        return  runPipeline("configs/pipelines/user-initial-pipeline.json", user);
    }

    public User runCompletionPipeline(User user) {
        return runPipeline("configs/pipelines/user-completition-pipeline.json", user);
    }

    private User runPipeline(String configPath, User user) {
        PipelineConfig pipelineConfig = loadPipelineConfig(configPath);

        for(PipelineStep step : pipelineConfig.steps()) {
            PipelineStepHandler handler = handlers.get(step.type());

            if (handler == null) {
                throw new PipelineException("No handler found for step type: " + step.type());
            }

            handler.handle(user);
        }

        return user;
    }

    private  PipelineConfig loadPipelineConfig(String configPath) {
        try {
            ClassPathResource resource = new ClassPathResource(configPath);

            try (InputStream inputStream = resource.getInputStream()) {
                return objectMapper.readValue(inputStream, PipelineConfig.class);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load pipeline config: " + configPath, exception);
        }
    }
}
