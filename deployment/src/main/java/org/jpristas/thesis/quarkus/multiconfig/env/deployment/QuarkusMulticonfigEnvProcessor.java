package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.runtime.Example;

class QuarkusMulticonfigEnvProcessor {

    private static final String FEATURE = "quarkus-multiconfig-env";
    private static final Logger LOGGER = Logger.getLogger(QuarkusMulticonfigEnvProcessor.class);


    @BuildStep
    FeatureBuildItem feature() {
        LOGGER.info("Logged text");
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem createExample() {
        return new AdditionalBeanBuildItem(Example.class);
    }
}
