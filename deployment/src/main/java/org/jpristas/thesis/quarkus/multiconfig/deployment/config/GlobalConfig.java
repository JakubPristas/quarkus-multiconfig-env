package org.jpristas.thesis.quarkus.multiconfig.deployment.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.List;
import java.util.Optional;

@ConfigRoot(name = "multiconfig", phase = ConfigPhase.BUILD_TIME)
public class GlobalConfig {

    /**
     * Target environment for generated files.
     */
    @ConfigItem(defaultValue = "dev")
    public Optional<String> targetEnvironment;

    /**
     * Selected templates for generated files (.env, .cm, .prop).
     */
    @ConfigItem
    public Optional<List<String>> selectedTemplates;

    /**
     * Output path for generated files in target folder.
     */
    @ConfigItem(defaultValue = "")
    public Optional<String> outputPath;

    /**
     * Name for .cm generated file.
     */
    @ConfigItem(defaultValue = "config.cm")
    public Optional<String> cmFileName;

    /**
     * Name for .env generated file.
     */
    @ConfigItem(defaultValue = ".env")
    public Optional<String> envFileName;

    /**
     * Name for .prop generated file.
     */
    @ConfigItem(defaultValue = "application.properties")
    public Optional<String> propertiesFileName;

    /**
     * Visibility description for each value in generated files.
     */
    @ConfigItem(defaultValue = "true")
    public Optional<Boolean> outputDescription;

    /**
     * Input source file path.
     */
    @ConfigItem(defaultValue = "src/main/resources/application.properties")
    public String sourceFilePath;

}
