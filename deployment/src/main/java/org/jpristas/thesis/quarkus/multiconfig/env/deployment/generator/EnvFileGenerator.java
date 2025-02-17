package org.jpristas.thesis.quarkus.multiconfig.env.deployment.generator;

import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;

import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.util.FileGenerationUtil;

import java.io.IOException;

public class EnvFileGenerator implements FileGenerator {
    private static final Logger LOG = Logger.getLogger(EnvFileGenerator.class);
//    private static final String TEMPLATE_PATH = "classpath:templates/velocity/template.env.vm";
    private static final String TEMPLATE_PATH = "/templates/qute/template.env.qute";

    //private static final String OUTPUT_FILE_NAME = "config.env";

    @Override
    public void generateFile(String fileContent, String targetEnvironment, String fileName, OutputTargetBuildItem outputTarget, BuildProducer<GeneratedResourceBuildItem> resourceProducer) throws IOException {
        FileGenerationUtil.generateFile(fileContent, TEMPLATE_PATH, fileName, targetEnvironment, outputTarget, resourceProducer);
    }
}
