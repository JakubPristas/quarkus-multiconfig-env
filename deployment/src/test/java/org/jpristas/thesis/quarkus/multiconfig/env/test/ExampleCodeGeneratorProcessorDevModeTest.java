//package org.jpristas.thesis.quarkus.multiconfig.env.test;
//
//import io.quarkus.test.QuarkusUnitTest;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.StringAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.jpristas.thesis.quarkus.multiconfig.env.deployment.ExampleCodeGeneratorProcessor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//import io.quarkus.test.QuarkusDevModeTest;
//
//public class ExampleCodeGeneratorProcessorDevModeTest {
//
//    @RegisterExtension
//    static final QuarkusDevModeTest unitTest = new QuarkusDevModeTest()
//            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));
//
//    @Test
//    public void testFileGeneration() throws IOException {
//        //String outputDir = System.getProperty("java.io.tmpdir") + "/quarkus-unit-test-output";
//        String projectBaseDir = System.getProperty("user.dir");
//        String outputDir = projectBaseDir + "/target";
//        //String outputDir = System.getProperty("user.dir") + "/integration-tests/target";
//
//
//        File generatedFile = new File(outputDir, ExampleCodeGeneratorProcessor.getFileName());
//
//        Assertions.assertTrue(generatedFile.exists(), "Generated file should exist");
//        Assertions.assertEquals(
//                ExampleCodeGeneratorProcessor.getFileContent(),
//                Files.readString(generatedFile.toPath()),
//                "File content should match");
//    }
//}
