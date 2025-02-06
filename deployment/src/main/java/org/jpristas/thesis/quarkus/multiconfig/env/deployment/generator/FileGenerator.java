package org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;

import java.io.IOException;

public interface FileGenerator {
    void generateFile(
            String fileContent,
            String targetEnvironment,
            String fileName,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedResourceBuildItem> resourceProducer
    ) throws IOException;
}
