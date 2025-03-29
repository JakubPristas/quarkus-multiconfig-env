package org.jpristas.thesis.quarkus.multiconfig.env.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusMulticonfigEnvProcessor {

    private static final String FEATURE = "quarkus-multiconfig-env";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
