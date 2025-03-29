package org.jpristas.thesis.quarkus.multiconfig.it.profile;

import io.quarkus.test.junit.QuarkusTestProfile;

public class Test4Profile implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "test4";
    }
}
