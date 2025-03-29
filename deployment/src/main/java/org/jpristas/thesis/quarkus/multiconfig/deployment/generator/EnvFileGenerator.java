package org.jpristas.thesis.quarkus.multiconfig.deployment.generator;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;

import org.jpristas.thesis.quarkus.multiconfig.deployment.util.FileGenerationUtil;

import java.io.IOException;

public class EnvFileGenerator implements FileGenerator {
    private static final String TEMPLATE_PATH = "/templates/qute/template.env.qute";

    @Override
    public void generateFile(
            String fileContent,
            String targetEnvironment,
            String fileName,
            boolean outputDescription,
            String outputPathProperty,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedResourceBuildItem> resourceProducer
    ) throws IOException {
        FileGenerationUtil.generateFile(
                fileContent,
                TEMPLATE_PATH,
                fileName,
                targetEnvironment,
                outputDescription,
                outputPathProperty,
                outputTarget,
                resourceProducer
        );
    }
}
