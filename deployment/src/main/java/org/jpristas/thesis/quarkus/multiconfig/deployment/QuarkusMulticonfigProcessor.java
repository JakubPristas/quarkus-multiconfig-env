package org.jpristas.thesis.quarkus.multiconfig.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusMulticonfigProcessor {

    private static final String FEATURE = "quarkus-multiconfig";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
