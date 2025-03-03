package org.jpristas.thesis.quarkus.multiconfig.env.it;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jpristas.thesis.quarkus.multiconfig.env.it.profile.Test2Profile;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(Test2Profile.class)
public class AnotherFileGeneratorTest {

    Path envFilePath = Paths.get("target/generated-sources/config-files/config.cm");

    @Test
    public void testGeneratedEnvFile() throws Exception {
        assertTrue(Files.exists(envFilePath),
                "The generated .env file should exist at " + envFilePath.toAbsolutePath());

        String content = Files.readString(envFilePath, StandardCharsets.UTF_8);

        // Validate that the file contains the expected description and property value.
        assertTrue(content.contains("MY_PROPERTY: '1'"),
                "The generated file should contain MY_PROPERTY_KEY with value 1 for the 'int' target environment");

    }

}
