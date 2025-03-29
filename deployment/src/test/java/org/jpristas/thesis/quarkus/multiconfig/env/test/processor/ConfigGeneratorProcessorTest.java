package org.jpristas.thesis.quarkus.multiconfig.env.test.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.config.GlobalConfig;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigGeneratorProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigGeneratorProcessorTest {

    private ConfigGeneratorProcessor processor;
    private BuildProducer<GeneratedResourceBuildItem> resourceProducer;
    private OutputTargetBuildItem outputTarget;

    private GlobalConfig globalConfig;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        processor = new ConfigGeneratorProcessor();
        resourceProducer = mock(BuildProducer.class);

        outputTarget = new OutputTargetBuildItem(
                tempDir,
                "test-base-name",
                "test-base-name",
                false,
                new Properties(),
                Optional.empty()
        );

        globalConfig = new GlobalConfig();
        globalConfig.targetEnvironment = Optional.empty();
        globalConfig.selectedTemplates = Optional.empty();
        globalConfig.outputPath = Optional.empty();
        globalConfig.cmFileName = Optional.empty();
        globalConfig.envFileName = Optional.empty();
        globalConfig.propertiesFileName = Optional.empty();
        globalConfig.outputDescription = Optional.empty();
    }

    @Test
    void testNoContent_skipsGeneration() throws IOException {
        ConfigDataBuildItem configData = new ConfigDataBuildItem("");

        processor.generateFiles(configData, outputTarget, resourceProducer, globalConfig);

        verifyNoInteractions(resourceProducer);
    }

    @Test
    void testGenerateAllTemplates() throws IOException {
        String fileContent = """
                quarkus.multiconfig.selected-templates=cm,env,prop
                quarkus.multiconfig.target-environment=dev
                quarkus.multiconfig.cm-file-name=config.cm
                quarkus.multiconfig.env-file-name=my.env
                quarkus.multiconfig.properties-file-name=application.properties
                quarkus.multiconfig.output-description=true
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        globalConfig.targetEnvironment = Optional.of("dev");
        globalConfig.selectedTemplates = Optional.of(List.of("cm", "env", "prop"));
        globalConfig.cmFileName = Optional.of("config.cm");
        globalConfig.envFileName = Optional.of("my.env");
        globalConfig.propertiesFileName = Optional.of("application.properties");
        globalConfig.outputDescription = Optional.of(true);

        processor.generateFiles(configData, outputTarget, resourceProducer, globalConfig);

        verify(resourceProducer, times(3)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testGenerateSomeTemplates() throws IOException {
        String fileContent = """
                quarkus.multiconfig.selected-templates=cm,prop
                quarkus.multiconfig.target-environment=stage
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        globalConfig.targetEnvironment = Optional.of("stage");
        globalConfig.selectedTemplates = Optional.of(List.of("cm", "prop"));

        processor.generateFiles(configData, outputTarget, resourceProducer, globalConfig);

        verify(resourceProducer, times(2)).produce(any(GeneratedResourceBuildItem.class));
    }

    @Test
    void testVerifyGeneratedResourceNames() throws IOException {
        String fileContent = """
                quarkus.multiconfig.selected-templates=cm
                quarkus.multiconfig.cm-file-name=myconfig.cm
                """;
        ConfigDataBuildItem configData = new ConfigDataBuildItem(fileContent);

        globalConfig.selectedTemplates = Optional.of(List.of("cm"));
        globalConfig.cmFileName = Optional.of("myconfig.cm");

        processor.generateFiles(configData, outputTarget, resourceProducer, globalConfig);

        ArgumentCaptor<GeneratedResourceBuildItem> captor =
                ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer, times(1)).produce(captor.capture());

        GeneratedResourceBuildItem producedResource = captor.getValue();
        assertEquals("myconfig.cm", producedResource.getName());
    }
}
