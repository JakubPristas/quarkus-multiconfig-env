package org.jpristas.thesis.quarkus.multiconfig.env.deployment.config;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropDocConfig {

    private final List<String> templatesToGenerate;
    private final String targetEnvironment;
    private final String cmFileName;
    private final String envFileName;
    private final String propFileName;
    private final String outputPath;
    private final boolean outputDescription;

    public PropDocConfig(String fileContent) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(fileContent));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }

        this.templatesToGenerate = Arrays.asList(
                properties.getProperty("prop-doc.selected-templates", "").split(",")
        );
        this.targetEnvironment = getPropertyOrDefault(properties,"prop-doc.target_environment", "dev");
        this.cmFileName = getPropertyOrDefault(properties, "prop-doc.cm.file-name", "default.cm");
        this.envFileName = getPropertyOrDefault(properties, "prop-doc.env.file-name", "default.env");
        this.propFileName = getPropertyOrDefault(properties, "prop-doc.prop.file-name", "default.properties");
        this.outputPath = properties.getProperty("prop-doc.output-path", "").trim();
        this.outputDescription = Boolean.parseBoolean(
                properties.getProperty("prop-doc.output-description", "false")
        );
    }

    private String getPropertyOrDefault(Properties properties, String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    public List<String> getTemplatesToGenerate() {
        return templatesToGenerate;
    }

    public String getTargetEnvironment() {
        return targetEnvironment;
    }

    public String getCmFileName() {
        return cmFileName;
    }

    public String getEnvFileName() {
        return envFileName;
    }

    public String getPropFileName() {
        return propFileName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public boolean isOutputDescription() {
        return outputDescription;
    }
}
