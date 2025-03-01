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
            prop-doc.selected-templates=cm,env
            prop-doc.target_environment=prod
            prop-doc.cm.file-name=config.cm
            prop-doc.env.file-name=config.env
            """;

        // Create a temporary file with our mock config
        Path tempFile = Files.createTempFile("config", ".properties");
        Files.writeString(tempFile, mockConfig);

        // Use a System property that ConfigReaderProcessor reads
        System.setProperty("example.config-reader.file.path", tempFile.toString());

        // Instantiate the processor directly
        ConfigReaderProcessor processor = new ConfigReaderProcessor();
        ConfigDataBuildItem configDataBuildItem = processor.readApplicationProperties();

        // Basic assertions
        assertNotNull(configDataBuildItem);
        assertEquals(mockConfig.trim(), configDataBuildItem.getFileContent().trim());
    }
}
