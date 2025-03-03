package org.jpristas.thesis.quarkus.multiconfig.env.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ConfigFileGeneratorTest {

    Path envFilePath = Paths.get("target/generated-sources/config-files/.env");

    @Test
    public void testGeneratedEnvFile() throws Exception {
        assertTrue(Files.exists(envFilePath),
                "The generated .env file should exist at " + envFilePath.toAbsolutePath());

        String content = Files.readString(envFilePath, StandardCharsets.UTF_8);

        // Validate that the file contains the expected description and property value.
        assertTrue(content.contains("#--- Description for my.property ---#"),
                "The generated file should contain the property description");
        assertTrue(content.contains("MY_PROPERTY=5"),
                "The generated file should contain MY_PROPERTY with value 5 for the 'int' target environment");

    }

}
