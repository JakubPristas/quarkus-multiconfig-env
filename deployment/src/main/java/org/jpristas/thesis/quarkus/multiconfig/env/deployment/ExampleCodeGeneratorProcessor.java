package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import ca.mrvisser.propdoc.VelocityMain;
import org.jboss.logging.Logger;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import ca.mrvisser.propdoc.api.PropDoc;
import ca.mrvisser.propdoc.api.PropDocWriter;
import ca.mrvisser.propdoc.impl.writer.VelocityPropDocWriterImpl;

public class ExampleCodeGeneratorProcessor {
    private static final Logger LOG = Logger.getLogger(ExampleCodeGeneratorProcessor.class);

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
            generateCmFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
        if (generateEnvFile) {
            generateEnvFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
        if (generatePropertiesFile) {
            generatePropertiesFile(fileContent, targetEnvironment, outputTarget, resourceProducer);
        }
    }

    private void generateCmFile(String fileContent, String targetEnvironment, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        generateFile(fileContent, "classpath:templates/template.cm.vm", "config.cm", targetEnvironment, outputTarget, resourceProducer);
    }

    private void generateEnvFile(String fileContent, String targetEnvironment, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        generateFile(fileContent, "classpath:templates/template.env.vm", "config.env", targetEnvironment, outputTarget, resourceProducer);
    }

    private void generatePropertiesFile(String fileContent, String targetEnvironment, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        generateFile(fileContent, "classpath:templates/template.properties.vm", "config.properties", targetEnvironment, outputTarget, resourceProducer);
    }

    private void generateFile(String fileContent, String templatePath, String outputFileName, String targetEnvironment,
                              OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {

        Path outputPath = Paths.get(outputTarget.getOutputDirectory().toString(), outputFileName);

        File outputFile = outputPath.toFile();
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            LOG.error("Failed to create directories for " + outputFile.getAbsolutePath());
            return;
        }

        // Process the content and write it using PropDocWriter
        try (OutputStream out = new FileOutputStream(outputFile)) {
            PropDoc propDoc = new PropDoc(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
            PropDocWriter propDocWriter = new VelocityPropDocWriterImpl(templatePath, targetEnvironment, false);
            propDocWriter.write(propDoc, out);
            LOG.info("File generated and processed with PropDoc successfully: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("Failed to generate file for template: " + templatePath, e);
        }

        // Produce the generated resource
        byte[] generatedFileContent = Files.readAllBytes(outputPath);
        resourceProducer.produce(new GeneratedResourceBuildItem(outputFileName, generatedFileContent));
        LOG.info("Resource registered successfully: " + outputFileName);
    }

}