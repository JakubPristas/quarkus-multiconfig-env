package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.impl.writer;

import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDocWriter;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.Property;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.util.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QutePropDocWriterImpl implements PropDocWriter {

    private static final String ALL_ENVIRONMENTS = "all";
    private static final String REQUIRED_KEY = "required";
    private static final String SUBSTITUTE_KEY_KEY = "key";

    private final String templateFile;
    private final String targetEnvironment;
    private final boolean outputDescription;

    // Qute engine can be re-used
    private static final Engine engine = Engine.builder().addDefaults().build();

    public QutePropDocWriterImpl(String templateFile, String targetEnvironment, boolean outputDescription) {
        this.templateFile = templateFile;
        this.targetEnvironment = targetEnvironment;
        this.outputDescription = outputDescription;
    }

    @Override
    public void write(PropDoc propDoc, OutputStream out) throws IOException {
        // 1. Build the data model (similar to buildContext in Velocity approach)
        Map<String, Object> context = buildContext(propDoc);

        // 2. Load the template content from the given path
        String templateContent = loadTemplateContent(templateFile);

        // 3. Parse and render using Qute
        Template template = engine.parse(templateContent);
        String rendered = template
                .data(context)
                .render();

        // 4. Write out the result
        try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            writer.write(rendered);
        }
    }

    private Map<String, Object> buildContext(PropDoc propDoc) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("propDoc", propDoc);

        List<ProdProperty> envEntries = propDoc.getProperties().entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(this::filterProperty)
                .map(this::mapToProdProperty)
                .toList();

        context.put("envVars", envEntries);
        context.put("allAttributes", getAllAttributes(propDoc));

        return context;
    }

    private boolean filterProperty(Property property) {
        // Same logic as in VelocityPropDocWriterImpl
        if (property.getMetadataValue("description") == null) {
            return false;
        }
        if (property.getMetadataValue(targetEnvironment) == null
                && property.getMetadataValue(ALL_ENVIRONMENTS) == null) {
            return false;
        }
        if (property.getMetadataValue(targetEnvironment) != null
                && property.getMetadataValue(targetEnvironment).isBlank()) {
            return false;
        }
        return true;
    }

    private ProdProperty mapToProdProperty(Property property) {
        String value = property.getMetadataValue(targetEnvironment);

        // Fallback to "all" if not set for the specific environment
        if (value == null) {
            value = property.getMetadataValue(ALL_ENVIRONMENTS);
        }

        boolean required = true;
        if (property.getMetadataKeys().contains(REQUIRED_KEY)) {
            required = Boolean.parseBoolean(property.getMetadataValue(REQUIRED_KEY));
        }

        String propertyKey = property.getKey();
        if (property.getMetadataKeys().contains(SUBSTITUTE_KEY_KEY)) {
            propertyKey = property.getMetadataValue(SUBSTITUTE_KEY_KEY);
        }

        String description = (outputDescription)
                ? property.getMetadataValue("description")
                : null;

        return new ProdProperty(propertyKey, property.getValue(), value, description, required);
    }

    private Iterable<String> getAllAttributes(PropDoc propDoc) {
        Set<String> allAttributes = new LinkedHashSet<>();
        for (Property property : propDoc) {
            allAttributes.addAll(property.getMetadataKeys());
        }
        return allAttributes;
    }

    private String loadTemplateContent(String templatePath) throws IOException {
        // We mimic ResourceUtil usage. Adjust as needed:
        try (InputStream in = ResourceUtil.createInputStreamFromUrl(templatePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Template not found: " + templatePath);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
