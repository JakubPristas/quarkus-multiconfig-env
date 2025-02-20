package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jpristas.thesis.quarkus.multiconfig.env.runtime.Example;

class QuarkusMulticonfigEnvProcessor {

    private static final String FEATURE = "quarkus-multiconfig-env";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem createExample() {
        return new AdditionalBeanBuildItem(Example.class);
    }
}
