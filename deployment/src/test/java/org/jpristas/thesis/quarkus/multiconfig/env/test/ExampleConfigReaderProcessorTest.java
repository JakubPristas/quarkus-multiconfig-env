package org.jpristas.thesis.quarkus.multiconfig.env.test;

import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.ExampleConfigReaderProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;


public class ExampleConfigReaderProcessorTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(ExampleConfigReaderProcessor.class)
                    .addAsResource("test-config-file.txt"));

    @Inject
    ExampleConfigReaderProcessor processor;

    @Test
    public void testReadApplicationProperties() throws IOException {

        processor.readApplicationProperties();

        Assertions.assertEquals("This is text from test-config-file", processor.getFileContent());
    }
}
