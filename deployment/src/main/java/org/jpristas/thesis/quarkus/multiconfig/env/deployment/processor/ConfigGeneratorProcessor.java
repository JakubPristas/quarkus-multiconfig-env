package org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.CmFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.EnvFileGenerator;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator.PropertiesFileGenerator;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class ConfigGeneratorProcessor {
    private static final Logger LOG = Logger.getLogger(ConfigGeneratorProcessor.class);

    @BuildStep
    void generateFiles(ConfigDataBuildItem configData, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        if (configData.getFileContent().isEmpty()) {
            LOG.warn("No configuration data available. Skipping file generation.");
            return;
        }

        Properties properties = new Properties();
        properties.load(new StringReader(configData.getFileContent()));

        boolean generateCmFile = Boolean.parseBoolean(properties.getProperty("quarkus.prop-doc.template_cm", "false"));
        boolean generateEnvFile = Boolean.parseBoolean(properties.getProperty("quarkus.prop-doc.template_env", "false"));
        boolean generatePropertiesFile = Boolean.parseBoolean(properties.getProperty("quarkus.prop-doc.template_properties", "false"));

        String targetEnvironment = properties.getProperty("quarkus.prop-doc.target_environment", "dev");

        String fileContent = configData.getFileContent();

        if (generateCmFile) {
            new CmFileGenerator().generateFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
        if (generateEnvFile) {
            new EnvFileGenerator().generateFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
        if (generatePropertiesFile) {
            new PropertiesFileGenerator().generateFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
    }
}
