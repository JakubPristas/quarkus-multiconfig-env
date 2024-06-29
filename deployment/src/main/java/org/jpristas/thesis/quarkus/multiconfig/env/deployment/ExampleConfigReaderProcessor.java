package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.arc.deployment.ConfigPropertyBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ExampleConfigReaderProcessor {

    @BuildStep
    @Produce(ConfigPropertyBuildItem.class)
    void readApplicationProperties(ApplicationArchivesBuildItem appArchives, BuildProducer<ConfigPropertyBuildItem> configPropertyProducer) {
        // Get the path to the application.properties file
        Path path = Path.of("src/main/resources/application.properties");

        // Read the properties file
        if (Files.exists(path)) {
            try {
                // Read all lines from the file
                System.out.println("Read application.properties file");
                Files.lines(path).forEach(line -> {
                    // Process each line

                    System.out.println(line);
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to read application.properties file", e);
            }
        } else {
            System.out.println("application.properties file not found");
        }
    }

}
