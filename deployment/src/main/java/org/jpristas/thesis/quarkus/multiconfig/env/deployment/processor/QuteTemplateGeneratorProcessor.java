package org.jpristas.thesis.quarkus.multiconfig.env.deployment.processor;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.GeneratedResourceBuildItem;
import io.quarkus.deployment.pkg.builditem.OutputTargetBuildItem;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import org.jboss.logging.Logger;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.builditem.ConfigDataBuildItem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuteTemplateGeneratorProcessor {

    private static final Logger LOG = Logger.getLogger(QuteTemplateGeneratorProcessor.class);

    @BuildStep
    void generateFromQuteTemplate(
            ConfigDataBuildItem configData,
            OutputTargetBuildItem outputTarget,
            BuildProducer<GeneratedResourceBuildItem> resourceProducer) {

        // 1) Check config data
        if (configData.getFileContent().isEmpty()) {
            LOG.warn("No config data found. Skipping Qute template generation.");
            return;
        }

        // 2) Parse some property from fileContent (demo)
        String fileContent = configData.getFileContent();
        String dbUser = parseProperty(fileContent, "quarkus.datasource.username");
        if (dbUser == null) {
            dbUser = "N/A";
        }

        // 3) Prepare Qute engine
        Engine engine = Engine.builder().addDefaults().build();
        Template template;

        // 4) Read template as a string
        try (InputStream is = this.getClass().getResourceAsStream("/templates/my-template.qute")) {
            if (is == null) {
                LOG.error("Template not found at /templates/my-template.qute");
                return;
            }
            String templateContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Parse the String
            template = engine.parse(templateContent);
            LOG.info("template: " + template);

        } catch (Exception e) {
            LOG.error("Failed to read Qute template from /templates/my-template.qute", e);
            return;
        }

        // 5) Render
        String renderedContent = template
                .data("dbUser", dbUser)
                .data("someKey", "Some dynamic value")
                .render();

        // 6) Decide on a file name
        String fileName = "qute-generated-output.txt";
        Path outputPath = Paths.get(outputTarget.getOutputDirectory().toString(), fileName);

        File outputFile = outputPath.toFile();
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            LOG.error("Failed to create output directories for: " + outputFile.getAbsolutePath());
            return;
        }

        try {
            // 7) Write content to the file
            Files.write(outputPath, renderedContent.getBytes(StandardCharsets.UTF_8));

            // 8) Produce resource
            resourceProducer.produce(new GeneratedResourceBuildItem(fileName, renderedContent.getBytes(StandardCharsets.UTF_8)));

            LOG.infof("Successfully generated Qute-based file at: %s", outputPath);
        } catch (Exception e) {
            LOG.errorf(e, "Failed to write Qute-generated content to %s", outputFile.getAbsolutePath());
        }
    }

    /**
     * Simple property parser.
     */
    private String parseProperty(String content, String key) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(bais))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith(key + "=")) {
                        return line.substring(line.indexOf('=') + 1).trim();
                    }
                }
            }
        } catch (Exception e) {
            LOG.debug("Failed to parse property: " + key, e);
        }
        return null;
    }
}
