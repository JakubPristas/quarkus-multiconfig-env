package org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.util.FileGenerationUtil;

import java.io.IOException;

public class CmFileGenerator implements FileGenerator {
    private static final Logger LOG = Logger.getLogger(CmFileGenerator.class);
    private static final String TEMPLATE_PATH = "classpath:templates/template.cm.vm";
    //private static final String OUTPUT_FILE_NAME = "config.cm";

    @Override
    public void generateFile(String fileContent, String targetEnvironment, String fileName, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        FileGenerationUtil.generateFile(fileContent, TEMPLATE_PATH, fileName, targetEnvironment, outputTarget, resourceProducer);
    }
}
