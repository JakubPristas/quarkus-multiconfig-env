package org.jpristas.thesis.quarkus.multiconfig.it;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jpristas.thesis.quarkus.multiconfig.it.profile.Test2Profile;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(Test2Profile.class)
public class CmFileGenerationForTestEnvironmentTest {

    Path filePath = Paths.get("target/generated-sources/config-files/cm/myconfig.yaml");

    @Test
    public void testGeneratedCmFileForTestEnv() throws Exception {
        assertTrue(Files.exists(filePath),
                "The generated myconfig.yaml file should exist at " + filePath.toAbsolutePath());

        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("my.property: 1"),
                "The generated file should contain my.property with value 1 for the 'test' target environment");

    }

}
