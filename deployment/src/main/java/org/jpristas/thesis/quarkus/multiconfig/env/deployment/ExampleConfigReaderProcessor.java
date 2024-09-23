package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.arc.deployment.ConfigPropertyBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Produce;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class ExampleConfigReaderProcessor {

    private static final Logger LOG = Logger.getLogger(ExampleConfigReaderProcessor.class);

    @BuildStep
    ConfigDataBuildItem readApplicationProperties() {

        Config config = ConfigProvider.getConfig();
        String configFilePath = config.getValue("example.config-reader.file.path", String.class);
        LOG.info("Config file path: " + configFilePath);
        Path path = Path.of(configFilePath);
        String fileContent = "";

        if (Files.exists(path)) {
            try {
                fileContent = Files.readString(path) + "\n# Database Configuration\nquarkus.datasource.username=dbuser";
                LOG.info("File content:\n" + fileContent);
                //dataProducer.produce(new ConfigDataBuildItem(fileContent));
            } catch (IOException e) {
                LOG.error("Failed to read file", e);
                fileContent = "";
            }
        } else {
            LOG.warn("File not found");
        }

        return new ConfigDataBuildItem(fileContent);
    }

//    public static String getFileContent() {
//        return fileContent;
//    }
}
