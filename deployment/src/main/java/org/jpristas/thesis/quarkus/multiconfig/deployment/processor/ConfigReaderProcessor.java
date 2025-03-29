package org.jpristas.thesis.quarkus.multiconfig.deployment.processor;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import org.jpristas.thesis.quarkus.multiconfig.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.deployment.config.GlobalConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ConfigReaderProcessor {

    @BuildStep
    public ConfigDataBuildItem readApplicationProperties(GlobalConfig multiConfig) {

        String configFilePath = multiConfig.sourceFilePath;

        Path path = Path.of(configFilePath);
        String fileContent = "";

        if (Files.exists(path)) {
            try {
                fileContent = Files.readString(path);
            } catch (IOException e) {
                Log.error("Failed to read file", e);
                fileContent = "";
            }
        } else {
            Log.warn("File not found");
        }

        return new ConfigDataBuildItem(fileContent);
    }
}
