package org.jpristas.thesis.quarkus.multiconfig.deployment.generator;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.deployment.util.FileGenerationUtil;

import java.io.IOException;

public class CmFileGenerator implements FileGenerator {
    private static final String TEMPLATE_PATH = "/templates/qute/template.cm.qute";

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
