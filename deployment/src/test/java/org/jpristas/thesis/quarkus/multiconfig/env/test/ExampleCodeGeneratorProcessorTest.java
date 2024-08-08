package org.jpristas.thesis.quarkus.multiconfig.env.test;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.ExampleCodeGeneratorProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.quarkus.test.QuarkusUnitTest;


public class ExampleCodeGeneratorProcessorTest {

    private static final String PROJECT_BASE_DIR = System.getProperty("user.dir");
    private static final String OUTPUT_DIR = PROJECT_BASE_DIR + "/target";
    private static final String FILE_NAME = ExampleCodeGeneratorProcessor.getFileName();
    private static final Path GENERATED_FILE_PATH = Paths.get(OUTPUT_DIR, FILE_NAME);

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(ExampleCodeGeneratorProcessor.class));

    @Test
    public void testFileGeneration() throws IOException {

        Assertions.assertTrue(Files.exists(GENERATED_FILE_PATH));
        Assertions.assertEquals(
                ExampleCodeGeneratorProcessor.getFileContent(),
                Files.readString(GENERATED_FILE_PATH),
                "File content should match");
    }
}
