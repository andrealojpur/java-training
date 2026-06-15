package org.example.pipeline;

import java.util.List;

public record PipelineConfig (
        String name,
        List<PipelineStep> steps
) { }
