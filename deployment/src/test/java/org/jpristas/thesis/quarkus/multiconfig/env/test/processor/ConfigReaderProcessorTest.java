package org.jpristas.thesis.quarkus.multiconfig.env.test.processor;

import static org.junit.jupiter.api.Assertions.*;

import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigReaderProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigReaderProcessorTest {

    @Test
    void testConfigReaderProcessor() throws IOException {
        String mockConfig = """
            quarkus.multiconfig.selected-templates=cm,env
            quarkus.multiconfig.target-environment=prod
            quarkus.multiconfig.cm-file-name=config.cm
            quarkus.multiconfig.env-file-name=config.env
            """;

        Path tempFile = Files.createTempFile("config", ".properties");
        Files.writeString(tempFile, mockConfig);

        System.setProperty("quarkus.multiconfig.source-file-path", tempFile.toString());

        ConfigReaderProcessor processor = new ConfigReaderProcessor();
        ConfigDataBuildItem configDataBuildItem = processor.readApplicationProperties();

        assertNotNull(configDataBuildItem);
        assertEquals(mockConfig.trim(), configDataBuildItem.getFileContent().trim());
    }
}
