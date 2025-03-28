package org.jpristas.thesis.quarkus.multiconfig.env.test.config;

import org.jpristas.thesis.quarkus.multiconfig.env.deployment.config.PropDocConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropDocConfigTest {

    @Test
    void testValidProperties() {
        String content = """
            quarkus.multiconfig.selected-templates=cm,env,prop
            quarkus.multiconfig.target-environment=prod
            quarkus.multiconfig.cm-file-name=myconfig.cm
            quarkus.multiconfig.env-file-name=myconfig.env
            quarkus.multiconfig.properties-file-name=myconfig.properties
            quarkus.multiconfig.output-description=true
            quarkus.multiconfig.output-path=/opt/app/config
            """;

        PropDocConfig config = new PropDocConfig(content);

        assertEquals(List.of("cm","env","prop"), config.getTemplatesToGenerate());

        assertEquals("prod", config.getTargetEnvironment());

        assertEquals("myconfig.cm", config.getCmFileName());
        assertEquals("myconfig.env", config.getEnvFileName());
        assertEquals("myconfig.properties", config.getPropFileName());

        assertTrue(config.isOutputDescription());

        assertEquals("/opt/app/config", config.getOutputPath());
    }

    @Test
    void testDefaults() {
        String content = "quarkus.multiconfig.selected-templates=cm";

        PropDocConfig config = new PropDocConfig(content);

        assertEquals("dev", config.getTargetEnvironment());
        assertEquals("default.cm", config.getCmFileName());
        assertEquals("default.env", config.getEnvFileName());
        assertEquals("default.properties", config.getPropFileName());
        assertFalse(config.isOutputDescription());
        assertEquals("", config.getOutputPath());
    }

    @Test
    void testEmptyTemplates() {
        String content = "quarkus.multiconfig.selected-templates=";

        PropDocConfig config = new PropDocConfig(content);

        assertEquals(List.of(""), config.getTemplatesToGenerate());
    }

    @Test
    void testInvalidProperties() {
        String content = "some malformed content without equals sign";

        PropDocConfig config = new PropDocConfig(content);

        assertTrue(config.getTemplatesToGenerate().isEmpty() ||
                config.getTemplatesToGenerate().equals(List.of("")));
    }
}
