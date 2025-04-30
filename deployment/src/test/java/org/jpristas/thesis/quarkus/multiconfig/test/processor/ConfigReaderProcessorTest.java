package org.jpristas.thesis.quarkus.multiconfig.test.processor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.jpristas.thesis.quarkus.multiconfig.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.deployment.config.GlobalConfig;
import org.jpristas.thesis.quarkus.multiconfig.deployment.processor.ConfigReaderProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class ConfigReaderProcessorTest {

    @Test
    void testConfigReaderProcessor() throws IOException {
        String mockConfig = """
            quarkus.multiconfig.selected-templates=cm,env
            quarkus.multiconfig.target-environment=prod
            quarkus.multiconfig.cm-file-name=config.yaml
            quarkus.multiconfig.env-file-name=config.env
            """;

        Path tempFile = Files.createTempFile("config", ".properties");
        Files.writeString(tempFile, mockConfig);

        GlobalConfig globalConfig = Mockito.mock(GlobalConfig.class);
        when(globalConfig.sourceFilePath()).thenReturn(tempFile.toString());

        ConfigReaderProcessor processor = new ConfigReaderProcessor();
        ConfigDataBuildItem configDataBuildItem = processor.readApplicationProperties(globalConfig);

        assertNotNull(configDataBuildItem);
        assertEquals(mockConfig.trim(), configDataBuildItem.getFileContent().trim());
    }
}
