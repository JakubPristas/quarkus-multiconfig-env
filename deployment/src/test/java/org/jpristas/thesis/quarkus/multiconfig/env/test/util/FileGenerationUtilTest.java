package org.jpristas.thesis.quarkus.multiconfig.env.test.util;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.logging.Log;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigGeneratorProcessor;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.util.FileGenerationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;


import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileGenerationUtilTest {

    private BuildProducer<GeneratedResourceBuildItem> resourceProducer;
    private OutputTargetBuildItem outputTarget;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        resourceProducer = mock(BuildProducer.class);

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
    public void shouldProduceGeneratedResourceItem() throws Exception {
        String fileContent = """
                ##
                # Description for my.property
                # @key my.property
                # @all 42
                ##
                my.property.key=42
                """;

        String templatePath = "/templates/qute/template.env.qute";
        String outputFileName = "test.env";
        String outputPathProperty = "test-output";
        String targetEnvironment = "prod";
        boolean outputDescription = true;

        FileGenerationUtil.generateFile(
                fileContent,
                templatePath,
                outputFileName,
                targetEnvironment,
                outputDescription,
                outputPathProperty,
                outputTarget,
                resourceProducer
        );

        ArgumentCaptor<GeneratedResourceBuildItem> captor = ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer).produce(captor.capture());

        GeneratedResourceBuildItem producedItem = captor.getValue();
        assertNotNull(producedItem);
        assertEquals(outputFileName, producedItem.getName());
    }
}

