package org.jpristas.thesis.quarkus.multiconfig.env.it;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jpristas.thesis.quarkus.multiconfig.env.it.profile.Test3Profile;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(Test3Profile.class)
public class PropFileGenerationForDevEnvironmentTest {
    Path filePath = Paths.get("target/generated-sources/config-files/prop/application.properties");

    @Test
    public void testGeneratedPropFileForDevEnv() throws Exception {
        assertTrue(Files.exists(filePath),
                "The generated application.properties file should exist at " + filePath.toAbsolutePath());

        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("#--- Description for my.property ---#"),
                "The generated file should contain the property description");
        assertTrue(content.contains("my.property=7"),
                "The generated file should contain my.property with value 7 for the 'dev' target environment");

    }
}
