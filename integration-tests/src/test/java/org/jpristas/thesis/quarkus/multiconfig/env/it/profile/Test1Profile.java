package org.jpristas.thesis.quarkus.multiconfig.env.it.profile;

import io.quarkus.test.junit.QuarkusTestProfile;

public class Test1Profile implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "test1";
    }
}
