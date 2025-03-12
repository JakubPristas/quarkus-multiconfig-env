package org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.config.PropDocConfig;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.CmFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.EnvFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.PropertiesFileGenerator;

import java.io.IOException;

public class ConfigGeneratorProcessor {
    private static final Logger LOG = Logger.getLogger(ConfigGeneratorProcessor.class);

    @BuildStep
    public void generateFiles(ConfigDataBuildItem configData, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        if (configData.getFileContent().isEmpty()) {
            LOG.warn("No configuration data available. Skipping file generation.");
            return;
        }

        PropDocConfig config = new PropDocConfig(configData.getFileContent());

        String fileContent = configData.getFileContent();

        if (config.getTemplatesToGenerate().contains("cm")) {
            new CmFileGenerator().generateFile(
                    fileContent,
                    config.getTargetEnvironment(),
                    config.getCmFileName(),
                    config.isOutputDescription(),
                    config.getOutputPath(),
                    outputTarget,
                    resourceProducer
            );
        }
        if (config.getTemplatesToGenerate().contains("env")) {
            new EnvFileGenerator().generateFile(
                    fileContent,
                    config.getTargetEnvironment(),
                    config.getEnvFileName(),
                    config.isOutputDescription(),
                    config.getOutputPath(),
                    outputTarget,
                    resourceProducer
            );
        }
        if (config.getTemplatesToGenerate().contains("prop")) {
            new PropertiesFileGenerator().generateFile(
                    fileContent,
                    config.getTargetEnvironment(),
                    config.getPropFileName(),
                    config.isOutputDescription(),
                    config.getOutputPath(),
                    outputTarget,
                    resourceProducer
            );
        }
    }
}
