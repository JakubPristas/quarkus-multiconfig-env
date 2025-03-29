package org.jpristas.thesis.quarkus.multiconfig.it;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.jpristas.thesis.quarkus.multiconfig.it.profile.Test4Profile;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(Test4Profile.class)
public class AllTemplatesFallbackToAllEnvTest {
    Path cmFilePath = Paths.get("target/generated-sources/config-files/all/default.cm");
    Path envFilePath = Paths.get("target/generated-sources/config-files/all/default.env");
    Path propFilePath = Paths.get("target/generated-sources/config-files/all/default.properties");


    @Test
    public void testGeneratedFilesWithDefaultNames() throws Exception {
        assertTrue(Files.exists(cmFilePath),
                "The generated default.cm file should exist at " + cmFilePath.toAbsolutePath());
        assertTrue(Files.exists(envFilePath),
                "The generated default.env file should exist at " + envFilePath.toAbsolutePath());
        assertTrue(Files.exists(propFilePath),
                "The generated default.properties file should exist at " + propFilePath.toAbsolutePath());
    }

    @Test
    public void testAllKey() throws Exception {
        String content = Files.readString(propFilePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("my.property=10"),
                "The generated file should contain my.property with value 10 for the 'prod' target environment");
    }

    @Test
    public void testOutputDescriptionToBeEmpty() throws Exception {
        String content = Files.readString(propFilePath, StandardCharsets.UTF_8);

        assertFalse(content.contains("#--- Description for my.property ---#"),
                "The generated file should not contain the property description");
    }
}
