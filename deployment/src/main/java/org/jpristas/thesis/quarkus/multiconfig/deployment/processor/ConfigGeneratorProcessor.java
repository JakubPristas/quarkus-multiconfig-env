package org.jpristas.thesis.quarkus.multiconfig.deployment.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.logging.Log;
import org.jpristas.thesis.quarkus.multiconfig.deployment.config.GlobalConfig;
import org.jpristas.thesis.quarkus.multiconfig.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.deployment.generator.CmFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.deployment.generator.EnvFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.deployment.generator.PropertiesFileGenerator;

import java.io.IOException;
import java.util.Collections;

public class ConfigGeneratorProcessor {

    public static final String CONFIGURATION_MAPPING_FILE_TYPE = "cm";
    public static final String ENVIRONMENT_VARIABLE_FILE_TYPE = "env";
    public static final String PROPERTIES_FILE_TYPE = "prop";
    @BuildStep
    public void generateFiles(
            ConfigDataBuildItem configData,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedResourceBuildItem> resourceProducer,
            GlobalConfig globalConfig
    ) throws IOException {
        if (configData.getFileContent().isEmpty()) {
            Log.warn("No configuration data available. Skipping file generation.");
            return;
        }

        String fileContent = configData.getFileContent();
        String effectiveTargetEnvironment = globalConfig.targetEnvironment().orElse("dev");
        String effectiveOutputPath = globalConfig.outputPath().orElse("");
        String effectiveCmFileName = globalConfig.cmFileName().orElse("ConfigMap.yaml");
        String effectiveEnvFileName = globalConfig.envFileName().orElse(".env");
        String effectivePropertiesFileName = globalConfig.propertiesFileName().orElse("application.properties");
        boolean effectiveOutputDescription = globalConfig.outputDescription().orElse(true);


        if (globalConfig.selectedTemplates().orElse(Collections.emptyList()).contains(CONFIGURATION_MAPPING_FILE_TYPE)) {
            new CmFileGenerator().generateFile(
                    fileContent,
                    effectiveTargetEnvironment,
                    effectiveCmFileName,
                    effectiveOutputDescription,
                    effectiveOutputPath,
                    outputTarget,
                    resourceProducer
            );
        }
        if (globalConfig.selectedTemplates().orElse(Collections.emptyList()).contains(ENVIRONMENT_VARIABLE_FILE_TYPE)) {
            new EnvFileGenerator().generateFile(
                    fileContent,
                    effectiveTargetEnvironment,
                    effectiveEnvFileName,
                    effectiveOutputDescription,
                    effectiveOutputPath,
                    outputTarget,
                    resourceProducer
            );
        }
        if (globalConfig.selectedTemplates().orElse(Collections.emptyList()).contains(PROPERTIES_FILE_TYPE)) {
            new PropertiesFileGenerator().generateFile(
                    fileContent,
                    effectiveTargetEnvironment,
                    effectivePropertiesFileName,
                    effectiveOutputDescription,
                    effectiveOutputPath,
                    outputTarget,
                    resourceProducer
            );
        }

    }
}
