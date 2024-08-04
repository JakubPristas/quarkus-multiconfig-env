package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.arc.deployment.ConfigPropertyBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExampleConfigReaderProcessor {

    private static final Logger LOG = Logger.getLogger(ExampleConfigReaderProcessor.class);

    private static String fileContent;

    @BuildStep
    @Produce(ConfigPropertyBuildItem.class)
    public void readApplicationProperties() {

        Config config = ConfigProvider.getConfig();
        String configFilePath = config.getValue("example.configreader.file.path", String.class);
        LOG.info("Config file path: " + configFilePath);
        Path path = Path.of(configFilePath);

        if (Files.exists(path)) {
            try {
                fileContent = Files.readString(path);
                LOG.info("File content:\n" + fileContent);
            } catch (IOException e) {
                LOG.error("Failed to read file", e);
                fileContent = "";
            }
        } else {
            LOG.warn("File not found");
        }
    }

    public String getFileContent() {
        return fileContent;
    }
}
