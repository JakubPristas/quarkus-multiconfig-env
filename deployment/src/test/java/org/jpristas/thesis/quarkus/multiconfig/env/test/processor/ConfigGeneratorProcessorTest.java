package org.jpristas.thesis.quarkus.multiconfig.env.test.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigGeneratorProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigGeneratorProcessorTest {

    private ConfigGeneratorProcessor processor;
    private BuildProducer<GeneratedResourceBuildItem> resourceProducer;
    private OutputTargetBuildItem outputTarget;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        processor = new ConfigGeneratorProcessor();
        resourceProducer = mock(BuildProducer.class);
//        outputTarget = mock(OutputTargetBuildItem.class);

        outputTarget = new OutputTargetBuildItem(
                tempDir,
                "test-base-name",
                "test-base-name",
                false,
                new Properties(),
                Optional.empty()
        );
    }

    @Test
    void testNoContent_skipsGeneration() throws IOException {
        ConfigDataBuildItem configData = new ConfigDataBuildItem("");

        processor.generateFiles(configData, outputTarget, resourceProducer);

        verifyNoInteractions(resourceProducer);
    }

    @Test
    void testGenerateAllTemplates() throws IOException {
        String fileContent = """
                prop-doc.selected-templates=cm,env,prop
                prop-doc.target_environment=dev
                prop-doc.cm.file-name=config.cm
                prop-doc.env.file-name=my.env
                prop-doc.prop.file-name=application.properties
                prop-doc.output-description=true
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        processor.generateFiles(configData, outputTarget, resourceProducer);

        verify(resourceProducer, times(3)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testGenerateSomeTemplates() throws IOException {
        String fileContent = """
                prop-doc.selected-templates=cm,prop
                prop-doc.target_environment=stage
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        processor.generateFiles(configData, outputTarget, resourceProducer);

        verify(resourceProducer, times(2)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testVerifyGeneratedResourceNames() throws IOException {
        String fileContent = """
                prop-doc.selected-templates=cm
                prop-doc.cm.file-name=myconfig.cm
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        processor.generateFiles(configData, outputTarget, resourceProducer);

        ArgumentCaptor<GeneratedResourceBuildItem> captor =
                ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer, times(1)).produce(captor.capture());

        GeneratedResourceBuildItem producedResource = captor.getValue();
        assertEquals("myconfig.cm", producedResource.getName());
    }
}
