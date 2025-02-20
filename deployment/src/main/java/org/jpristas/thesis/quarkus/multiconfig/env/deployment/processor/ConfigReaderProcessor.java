package org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor;

import io.quarkus.deployment.annotations.BuildStep;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ConfigReaderProcessor {

    private static final Logger LOG = Logger.getLogger(ConfigReaderProcessor.class);

    @BuildStep
    ConfigDataBuildItem readApplicationProperties() {

        Config config = ConfigProvider.getConfig();
        String configFilePath = config.getValue("example.config-reader.file.path", String.class);
        Path path = Path.of(configFilePath);
        String fileContent = "";

        if (Files.exists(path)) {
            try {
                fileContent = Files.readString(path);
            } catch (IOException e) {
                LOG.error("Failed to read file", e);
                fileContent = "";
            }
        } else {
            LOG.warn("File not found");
        }

        return new ConfigDataBuildItem(fileContent);
    }
}
