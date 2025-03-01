//package org.jpristas.thesis.quarkus.multiconfig.env.test.processor;
//
//import io.quarkus.deployment.annotations.BuildProducer;
//import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
//import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
//import io.quarkus.test.Mock;
//import io.quarkus.test.InjectMock;
//import io.quarkus.test.QuarkusUnitTest;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;
//import org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor.ConfigGeneratorProcessor;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//
//public class ConfigGeneratorProcessorTest {
//    @RegisterExtension
//    static final QuarkusUnitTest config = new QuarkusUnitTest()
//            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
//                    .addClass(ConfigGeneratorProcessor.class)
//                    .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"));
//
////    @InjectMock
////    ConfigGeneratorProcessor configGeneratorProcessor;
//
////    ConfigGeneratorProcessor configGeneratorProcessor = new ConfigGeneratorProcessor();
//
////    @Mock
////    BuildProducer<GeneratedResourceBuildItem> resourceProducer;
////    BuildProducer<GeneratedResourceBuildItem> resourceProducer = mock(BuildProducer.class);
//
////    @Mock
////    OutputTargetBuildItem outputTarget;
////    OutputTargetBuildItem outputTarget = mock(OutputTargetBuildItem.class);
//
//    @Test
//    void testGenerateFiles() throws IOException {
//        ConfigGeneratorProcessor configGeneratorProcessor = new ConfigGeneratorProcessor();
//        OutputTargetBuildItem outputTarget = mock(OutputTargetBuildItem.class);
//        BuildProducer<GeneratedResourceBuildItem> resourceProducer = mock(BuildProducer.class);
//
//        String mockConfig = "prop-doc.selected-templates=cm,env,prop\n" +
//                "prop-doc.target_environment=prod\n" +
//                "prop-doc.cm.file-name=config.cm\n" +
//                "prop-doc.env.file-name=config.env\n" +
//                "prop-doc.prop.file-name=config.properties\n";
//
//        ConfigDataBuildItem configData = new ConfigDataBuildItem(mockConfig);
//
//        configGeneratorProcessor.generateFiles(configData, outputTarget, resourceProducer);
//
//        verify(resourceProducer, atLeastOnce()).produce(any(GeneratedResourceBuildItem.class));
//    }
//}
