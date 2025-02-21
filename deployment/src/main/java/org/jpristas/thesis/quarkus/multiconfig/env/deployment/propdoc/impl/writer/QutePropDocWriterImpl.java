package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.impl.writer;

import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDocWriter;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.Property;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QutePropDocWriterImpl implements PropDocWriter {

    private static final String ALL_ENVIRONMENTS = "all";
    private static final String REQUIRED_KEY = "required";
    private static final String SUBSTITUTE_KEY_KEY = "key";
    private String templateFile;
    private String targetEnvironment;
    private boolean outputDescription;

    public QutePropDocWriterImpl(String templateFile, String targetEnvironment, boolean outputDescription) {
        this.templateFile = templateFile;
        this.targetEnvironment = targetEnvironment;
        this.outputDescription = outputDescription;
    }

    @Override
    public void write(PropDoc propDoc, OutputStream out) throws IOException {
        Map<String, Object> context = buildContext(propDoc);

        //Log.info("buildContext: " + context);
        Engine engine = Engine.builder()
                .addDefaults()
                .build();
        Template template;
        //Log.info("templateFile: " + templateFile);
        //try (InputStream is = getClass().getResourceAsStream("/templates/testing-template.qute")) {
        try (InputStream is = getClass().getResourceAsStream(templateFile)) {

                if (is == null) {
                throw new IOException("Template file not found: " + templateFile);
            }
            String templateContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            template = engine.parse(templateContent);
        }

        List<Map<String, Object>> mappedEnvVars = new ArrayList<>();
        for (ProdProperty property : (List<ProdProperty>) context.get("envVars")) {
            Map<String, Object> propMap = new HashMap<>();
            propMap.put("originalName", property.getOriginalName());
            propMap.put("name", property.getName());
            propMap.put("originalValue", property.getOriginalValue());
            propMap.put("prodValue", property.getProdValue());
            propMap.put("description", property.getDescription());
            propMap.put("required", property.isRequired());
            mappedEnvVars.add(propMap);
        }

        String renderedContent = template
                .data("propDoc", context.get("propDoc"))
                .data("envVars", mappedEnvVars)
                .data("allAttributes", context.get("allAttributes"))
                .render();

        try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            writer.write(renderedContent);
        }
    }


    private Map<String, Object> buildContext(PropDoc propDoc) {
        Map<String, Object> context = new LinkedHashMap<>();
        // PropDoc.print(propDoc);
        Iterable<String> allAttributes = getAllAttributes(propDoc);

        List<ProdProperty> envEntries = propDoc.getProperties().entrySet().stream().map(Map.Entry::getValue)
                .filter(this::filterProperty).map(this::mapToProdProperty).toList();

        context.put("propDoc", propDoc);
        context.put("envVars", envEntries);
        context.put("allAttributes", allAttributes);
        return context;
    }

    private boolean filterProperty(Property property) {
        if (property.getMetadataValue("description") == null) {
            return false;
        }
        String logMessage = property.getKey(); // + " = " + property.getValue();
        boolean required = true;
        if (property.getMetadataKeys().contains(REQUIRED_KEY)) {
            required = Boolean.parseBoolean(property.getMetadataValue(REQUIRED_KEY));
        }

        if (property.getMetadataValue(targetEnvironment) == null
                && property.getMetadataValue(ALL_ENVIRONMENTS) == null) {
            //Log.info("[SKIP - '@{}' value missing] {}", targetEnvironment, logMessage);
            return false;
        }

        if (property.getMetadataValue(targetEnvironment) != null
                && property.getMetadataValue(targetEnvironment).isBlank()) {
            //Log.info("[SKIP - '@{} and @{}' targets ignored] {}", targetEnvironment, ALL_ENVIRONMENTS,
            //        logMessage);
            return false;
        }

        //Log.info("[ADDED] {}{}", logMessage, (required ? " (required)" : ""));
        return true;
    }

    private ProdProperty mapToProdProperty(Property property) {
        String value = property.getMetadataValue(targetEnvironment);
        String description = null;
        if (outputDescription) {
            description = property.getMetadataValue("description");
        }

        int mainTargetDelimiterPos = targetEnvironment.indexOf("+");
        if (mainTargetDelimiterPos > 0) {
            String mainTargetEnvironment = targetEnvironment.substring(0, mainTargetDelimiterPos);
            if (value == null && !targetEnvironment.equals(mainTargetEnvironment)) {
                value = property.getMetadataValue(mainTargetEnvironment);
            }
        }

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
        // ProdProperty co = new ProdProperty(property.getKey(), property.getValue(), value, description, required);
        // System.out.println("[WRITE] " + co.getName() + "=" + co.getValue() + "\n");
        // return co;
        return new ProdProperty(propertyKey, property.getValue(), value, description, required);
    }

    private Iterable<String> getAllAttributes(PropDoc propDoc) {
        Set<String> allAttributes = new LinkedHashSet<>();
        for (Property property : propDoc) {
            allAttributes.addAll(property.getMetadataKeys());
        }
        return allAttributes;
    }

    public String getTemplateFile() {
        return templateFile;
    }

}


