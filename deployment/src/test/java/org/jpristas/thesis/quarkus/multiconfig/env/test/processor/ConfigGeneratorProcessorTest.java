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
                tempDir,            // Path outputDirectory
                "test-base-name",   // baseName
                "test-base-name",   // originalBaseName
                false,              // rebuild
                new Properties(),
                Optional.empty()
        );
    }

    @Test
    void testNoContent_skipsGeneration() throws IOException {
        // Given empty content
        ConfigDataBuildItem configData = new ConfigDataBuildItem("");

        // When
        processor.generateFiles(configData, outputTarget, resourceProducer);

        // Then: no interactions
        verifyNoInteractions(resourceProducer);
    }

    @Test
    void testGenerateAllTemplates() throws IOException {
        // Given config data that selects cm, env, prop
        String fileContent = """
                prop-doc.selected-templates=cm,env,prop
                prop-doc.target_environment=dev
                prop-doc.cm.file-name=configmap.yaml
                prop-doc.env.file-name=my.env
                prop-doc.prop.file-name=application.properties
                prop-doc.output-description=true
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        // When
        processor.generateFiles(configData, outputTarget, resourceProducer);

        // Then: we expect 3 calls to produce() (one for each template)
        verify(resourceProducer, times(3)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testGenerateSomeTemplates() throws IOException {
        // Given only cm and prop are selected
        String fileContent = """
                prop-doc.selected-templates=cm,prop
                prop-doc.target_environment=stage
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        // When
        processor.generateFiles(configData, outputTarget, resourceProducer);

        // Then: 2 calls (cm + prop)
        verify(resourceProducer, times(2)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testVerifyGeneratedResourceNames() throws IOException {
        // Suppose we only generate cm to check the resource name
        String fileContent = """
                prop-doc.selected-templates=cm
                prop-doc.cm.file-name=myconfigmap.yaml
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        processor.generateFiles(configData, outputTarget, resourceProducer);

        // Capture the GeneratedResourceBuildItem argument to verify resource name
        ArgumentCaptor<GeneratedResourceBuildItem> captor =
                ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer, times(1)).produce(captor.capture());

        GeneratedResourceBuildItem producedResource = captor.getValue();
        // If FileGenerationUtil sets the item name to match the fileName,
        // we can check it here:
        assertEquals("myconfigmap.yaml", producedResource.getName());
    }
}
