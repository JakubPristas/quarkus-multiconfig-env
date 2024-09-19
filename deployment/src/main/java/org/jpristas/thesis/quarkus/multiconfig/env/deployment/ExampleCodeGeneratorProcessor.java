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

    @BuildStep
    void generateFile(ConfigDataBuildItem configData, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        if (configData.getFileContent().isEmpty()) {
            LOG.warn("No configuration data available. Skipping file generation.");
            return;
        }

        LOG.info("Generating file...");
        File targetFile = new File(outputTarget.getOutputDirectory().toFile(), FILE_NAME);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(targetFile)) {
            writer.write(configData.getFileContent());
            LOG.info("Text file generated successfully: " + targetFile.getAbsolutePath());
        }

        resourceProducer.produce(new GeneratedResourceBuildItem(FILE_NAME, configData.getFileContent().getBytes()));
    }

//    public static String getFileName() {
//        return FILE_NAME;
//    }
//
//    public static String getFileContent() {
//        return FILE_CONTENT;
//    }

}
