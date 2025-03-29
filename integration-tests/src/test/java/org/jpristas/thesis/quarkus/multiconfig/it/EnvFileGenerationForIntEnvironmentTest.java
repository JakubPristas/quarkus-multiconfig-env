package org.jpristas.thesis.quarkus.multiconfig.it;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jpristas.thesis.quarkus.multiconfig.it.profile.Test1Profile;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(Test1Profile.class)
public class EnvFileGenerationForIntEnvironmentTest {

    Path filePath = Paths.get("target/generated-sources/config-files/env/my.env");

    @Test
    public void testGeneratedEnvFileForIntEnv() throws Exception {
        assertTrue(Files.exists(filePath),
                "The generated my.env file should exist at " + filePath.toAbsolutePath());

        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("#--- Description for my.property ---#"),
                "The generated file should contain the property description");
        assertTrue(content.contains("MY_PROPERTY=5"),
                "The generated file should contain MY_PROPERTY with value 5 for the 'int' target environment");

    }

}
