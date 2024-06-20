package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ExampleCodeGeneratorProcessor {

    private static final Logger LOG = Logger.getLogger(ExampleCodeGeneratorProcessor.class);

    private static final String FILE_NAME = "generated-data.txt";
    private static final String FILE_CONTENT = "This text file was generated by your Quarkus extension.";

    @BuildStep
    void generateFile(OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        LOG.info("Generating file...");
        File targetFile = new File(outputTarget.getOutputDirectory().toFile(), FILE_NAME);
        File targetDir = targetFile.getParentFile();

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(targetFile)) {
            writer.write(FILE_CONTENT);
            LOG.info("Text file generated successfully: " + targetFile.getAbsolutePath());
        }

        // Produce the build item
        resourceProducer.produce(new GeneratedResourceBuildItem(FILE_NAME, FILE_CONTENT.getBytes()));
    }

}
