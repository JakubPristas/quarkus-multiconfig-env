package org.jpristas.thesis.quarkus.multiconfig.deployment.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "quarkus.multiconfig")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface GlobalConfig {

    /**
     * Target environment for generated files.
     */
    @WithDefault("dev")
    Optional<String> targetEnvironment();

    /**
     * Selected templates for generated files (.env, cm, .prop).
     */
    @WithDefault("")
    Optional<List<String>> selectedTemplates();

    /**
     * Output path for generated files in target folder.
     */
    @WithDefault("")
    Optional<String> outputPath();

    /**
     * Name for cm generated file.
     */
    @WithDefault("ConfigMap.yaml")
    Optional<String> cmFileName();

    /**
     * Name for .env generated file.
     */
    @WithDefault(".env")
    Optional<String> envFileName();

    /**
     * Name for .prop generated file.
     */
    @WithDefault("application.properties")
    Optional<String> propertiesFileName();

    /**
     * Visibility description for each value in generated files.
     */
    @WithDefault("true")
    Optional<Boolean> outputDescription();

    /**
     * Input source file path.
     */
    @WithDefault("src/main/resources/application.properties")
    String sourceFilePath();
}
