package org.jpristas.thesis.quarkus.multiconfig.env.deployment.util;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDocWriter;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.impl.writer.QutePropDocWriterImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileGenerationUtil {
    private static final Logger LOG = Logger.getLogger(FileGenerationUtil.class);

    public static void generateFile(
            String fileContent,
            String templatePath,
            String outputFileName,
            String targetEnvironment,
            boolean outputDescription,
            String outputPathProperty,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedResourceBuildItem> resourceProducer
    ) throws IOException {

        Path baseOutputDir = Paths.get(outputTarget.getOutputDirectory().toString());
        Path customOutputPath = outputPathProperty.isEmpty() ? baseOutputDir : baseOutputDir.resolve(outputPathProperty);
        Path outputPath = customOutputPath.resolve(outputFileName);

        File outputFile = outputPath.toFile();
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            LOG.error("Failed to create directories for " + outputFile.getAbsolutePath());
            return;
        }

        try (OutputStream out = new FileOutputStream(outputFile)) {
            PropDoc propDoc = new PropDoc(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
            propDoc.print(propDoc);
            PropDocWriter propDocWriter = new QutePropDocWriterImpl(templatePath, targetEnvironment, outputDescription);
            propDocWriter.write(propDoc, out);
            LOG.info("File generated and processed with PropDoc successfully: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("Failed to generate file for template: " + templatePath, e);
        }

        byte[] generatedFileContent = Files.readAllBytes(outputPath);
        resourceProducer.produce(new GeneratedResourceBuildItem(outputFileName, generatedFileContent));
        LOG.info("Resource registered successfully: " + outputFileName);
    }
}
