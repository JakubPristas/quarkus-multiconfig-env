package org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ConfigReaderProcessor {

    @BuildStep
    public ConfigDataBuildItem readApplicationProperties() {

        Config config = ConfigProvider.getConfig();
        String configFilePath = config.getValue("example.config-reader.file.path", String.class);
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
