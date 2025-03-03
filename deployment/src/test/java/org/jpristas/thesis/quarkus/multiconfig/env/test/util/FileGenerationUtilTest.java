package org.jpristas.thesis.quarkus.multiconfig.env.test.util;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.util.FileGenerationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class FileGenerationUtilTest {

    @TempDir
    Path tempDir;

    BuildProducer<GeneratedResourceBuildItem> resourceProducer;

    @BeforeEach
    void setup() {
        resourceProducer = mock(BuildProducer.class);
    }

    @Test
    void testGenerateFile() throws IOException {
        OutputTargetBuildItem outputTarget = new OutputTargetBuildItem(
                tempDir,
                "test-base-name",
                "test-base-name",
                false,
                new Properties(),
                Optional.empty()
        );

        String fileContent = """
                key1=value1
                key2=value2
                """;

        String templatePath = "/templates/qute/template.cm.qute";
        String outputFileName = "config.cm";
        String targetEnvironment = "dev";
        boolean outputDescription = true;
        String outputPathProperty = "subfolder";

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

        Path expectedSubDir = tempDir.resolve("subfolder");
        Path expectedFile = expectedSubDir.resolve(outputFileName);
        assertTrue(Files.exists(expectedFile), "Expected generated file does not exist: " + expectedFile);

        byte[] actualBytes = Files.readAllBytes(expectedFile);
        assertTrue(actualBytes.length > 0, "Generated file is empty, expected some content.");

        ArgumentCaptor<GeneratedResourceBuildItem> captor =
                ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer, times(1)).produce(captor.capture());

        GeneratedResourceBuildItem producedItem = captor.getValue();
        assertEquals(outputFileName, producedItem.getName());

        assertArrayEquals(actualBytes, producedItem.getData(),
                "The bytes in the GeneratedResourceBuildItem do not match the file's bytes");
    }

    @Test
    void testGenerateFileFailsToCreateDirectory() throws IOException {
        OutputTargetBuildItem outputTarget = new OutputTargetBuildItem(
                tempDir,
                "test-base-name",
                "test-base-name",
                false,
                new Properties(),
                Optional.empty()
        );
        Path existingFile = tempDir.resolve("already-a-file");
        Files.createFile(existingFile);

        String outputPathProperty = "already-a-file/some-subdir";

        FileGenerationUtil.generateFile(
                "some content",
                "/templates/qute/template.env.qute",
                "my.env",
                "dev",
                false,
                outputPathProperty,
                outputTarget,
                resourceProducer
        );

        verify(resourceProducer, never()).produce(any(GeneratedResourceBuildItem.class));
    }
}

