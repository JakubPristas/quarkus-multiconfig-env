package org.jpristas.thesis.quarkus.multiconfig.env.test.util;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
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

/**
 * Plain JUnit test for FileGenerationUtil.generateFile(...).
 * We verify it creates the expected file on disk and calls the resource producer with the correct bytes.
 */
class FileGenerationUtilTest {

    @TempDir
    Path tempDir; // JUnit creates a temp directory that is automatically cleaned

    BuildProducer<GeneratedResourceBuildItem> resourceProducer;

    @BeforeEach
    void setup() {
        resourceProducer = mock(BuildProducer.class);
    }

    @Test
    void testGenerateFile() throws IOException {
        // Arrange
        // We'll create a minimal OutputTargetBuildItem pointing to our temp dir
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

        String templatePath = "/templates/qute/template.cm.qute"; // an example template
        String outputFileName = "my-configmap.yaml";
        String targetEnvironment = "dev";
        boolean outputDescription = true;
        String outputPathProperty = "subfolder"; // We'll test that a subdirectory is created

        // Act
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

        // Assert
        // 1) The file should exist on disk
        Path expectedSubDir = tempDir.resolve("subfolder");
        Path expectedFile = expectedSubDir.resolve(outputFileName);
        assertTrue(Files.exists(expectedFile), "Expected generated file does not exist: " + expectedFile);

        // 2) We can check that it has some content (the Qute template logic might modify it,
        //    but let's at least confirm something was written).
        //    For simplicity, we just check it's not empty.
        byte[] actualBytes = Files.readAllBytes(expectedFile);
        assertTrue(actualBytes.length > 0, "Generated file is empty, expected some content.");

        // 3) Verify resourceProducer was called
        ArgumentCaptor<GeneratedResourceBuildItem> captor =
                ArgumentCaptor.forClass(GeneratedResourceBuildItem.class);
        verify(resourceProducer, times(1)).produce(captor.capture());

        GeneratedResourceBuildItem producedItem = captor.getValue();
        // Check the resource name matches the file name
        assertEquals(outputFileName, producedItem.getName());

        // Check the produced bytes match what's on disk
        assertArrayEquals(actualBytes, producedItem.getData(),
                "The bytes in the GeneratedResourceBuildItem do not match the file's bytes");
    }

    @Test
    void testGenerateFileFailsToCreateDirectory() throws IOException {
        // Suppose we pass an invalid outputPathProperty or we deny directory creation in some environment
        // It's tricky to force a directory creation failure in a normal environment, but let's at least
        // show how we'd test it.

        // We'll just spy on FileGenerationUtil or the log if we want to confirm an error is logged.
        // For demonstration, let's assume it gracefully returns if mkdir fails.

        OutputTargetBuildItem outputTarget = new OutputTargetBuildItem(
                tempDir,
                "test-base-name",
                "test-base-name",
                false,
                new Properties(),
                Optional.empty()
        );
        // We can simulate the creation failure by pointing to a file that already exists
        // and can't be used as a directory. For example:
        Path existingFile = tempDir.resolve("already-a-file");
        Files.createFile(existingFile); // Now this path is a file, not a directory

        String outputPathProperty = "already-a-file/some-subdir"; // will fail on mkdirs

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

        // If the directory creation fails, the method logs an error and returns early without producing.
        verify(resourceProducer, never()).produce(any(GeneratedResourceBuildItem.class));
    }
}

