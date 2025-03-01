package org.jpristas.thesis.quarkus.multiconfig.env.test.processor;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.QuarkusUnitTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigReaderProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigReaderProcessorTest {
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(ConfigReaderProcessor.class)
                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"));

    @Test
    void testConfigReaderProcessor() throws IOException {
        String mockConfig = "prop-doc.selected-templates=cm,env\n" +
                "prop-doc.target_environment=prod\n" +
                "prop-doc.cm.file-name=config.cm\n" +
                "prop-doc.env.file-name=config.env\n";

        Path tempFile = Files.createTempFile("config", ".properties");
        Files.writeString(tempFile, mockConfig);

        System.setProperty("example.config-reader.file.path", tempFile.toString());

        ConfigReaderProcessor processor = new ConfigReaderProcessor();
        ConfigDataBuildItem configDataBuildItem = processor.readApplicationProperties();
        assertNotNull(configDataBuildItem);
        assertEquals(mockConfig.trim(), configDataBuildItem.getFileContent().trim());
    }
}
