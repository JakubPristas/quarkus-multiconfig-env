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
import java.util.Arrays;
import java.util.List;
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

        String selectedTemplates = properties.getProperty("quarkus.prop-doc.selected-templates", "");
        List<String> templatesToGenerate = Arrays.asList(selectedTemplates.split(","));

        String targetEnvironment = properties.getProperty("quarkus.prop-doc.target_environment", "dev");

        String cmFileName = properties.getProperty("quarkus.prop-doc.cm.file-name");
        if (cmFileName == null || cmFileName.trim().isEmpty()) {
            cmFileName = "default.cm";
        }
        String envFileName = properties.getProperty("quarkus.prop-doc.env.file-name");
        if (envFileName == null || envFileName.trim().isEmpty()) {
            envFileName = "default.env";
        }
        String propFileName = properties.getProperty("quarkus.prop-doc.prop.file-name");
        if (propFileName == null || propFileName.trim().isEmpty()) {
            propFileName = "default.properties";
        }
        // TODO
        String outputPathProperty = properties.getProperty("quarkus.prop-doc.output-path", "").trim();
        boolean outputDescription = Boolean.parseBoolean(properties.getProperty("quarkus.prop-doc.output-description", "false"));

        String fileContent = configData.getFileContent();

        if (templatesToGenerate.contains("cm")) {
            new CmFileGenerator().generateFile(
                    fileContent,
                    targetEnvironment,
                    cmFileName,
                    outputDescription,
                    outputPathProperty,
                    outputTarget,
                    resourceProducer
            );
        }
        if (templatesToGenerate.contains("env")) {
            new EnvFileGenerator().generateFile(
                    fileContent,
                    targetEnvironment,
                    envFileName,
                    outputDescription,
                    outputPathProperty,
                    outputTarget,
                    resourceProducer
            );
        }
        if (templatesToGenerate.contains("prop")) {
            new PropertiesFileGenerator().generateFile(
                    fileContent,
                    targetEnvironment,
                    propFileName,
                    outputDescription,
                    outputPathProperty,
                    outputTarget,
                    resourceProducer
            );
        }
    }
}
