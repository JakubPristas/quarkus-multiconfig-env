package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    //private static final String FILE_NAME = ".env";
    private static final String FILE_NAME = "commons.properties";

    private static final String VELOCITY_TEMPLATE_URL = "classpath:templates/template.properties.vm";

    //private static final String VELOCITY_TEMPLATE_URL = "classpath:templates/template.env.vm";

    @BuildStep
    void generateFile(ConfigDataBuildItem configData, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException, URISyntaxException {
        if (configData.getFileContent().isEmpty()) {
            LOG.warn("No configuration data available. Skipping file generation.");
            return;
        }

        Path outputPath = Paths.get(outputTarget.getOutputDirectory().toString(), FILE_NAME);

        // Create the file and ensure parent directories exist
        File outputFile = outputPath.toFile();
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            LOG.error("Failed to create directories for " + outputFile.getAbsolutePath());
            return;
        }

        // Process the content and write it using PropDocWriter
        try (OutputStream out = new FileOutputStream(outputFile)) {
            PropDoc propDoc = new PropDoc(new ByteArrayInputStream(configData.getFileContent().getBytes(StandardCharsets.UTF_8)));
            PropDocWriter propDocWriter = new VelocityPropDocWriterImpl(VELOCITY_TEMPLATE_URL, "dev", false);
            propDocWriter.write(propDoc, out);
            LOG.info("File generated and processed with PropDoc successfully: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("Failed to generate file", e);
            return;
        }

        // Read the generated file content back into a byte array
        byte[] fileContent = Files.readAllBytes(outputPath);

        // Produce the generated resource
        resourceProducer.produce(new GeneratedResourceBuildItem(FILE_NAME, fileContent));
        LOG.info("Resource registered successfully: " + FILE_NAME);
    }
}