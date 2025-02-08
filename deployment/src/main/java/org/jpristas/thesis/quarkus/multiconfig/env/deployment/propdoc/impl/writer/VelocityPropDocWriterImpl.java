package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.impl.writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDocWriter;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.Property;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.util.ResourceUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class VelocityPropDocWriterImpl implements PropDocWriter {
    private static final String ALL_ENVIRONMENTS = "all";
    private static final String REQUIRED_KEY = "required";
    private static final String SUBSTITUTE_KEY_KEY = "key";

    private String templateFile;
    private String targetEnvironment;
    private boolean outputDescription;

    public VelocityPropDocWriterImpl(String templateFile, String targetEnvironment,
                                     boolean outputDescription) {
        this.templateFile = templateFile;
        this.targetEnvironment = targetEnvironment;
        this.outputDescription = outputDescription;
    }

    @Override
    public void write(PropDoc propDoc, OutputStream out) throws IOException {
        Map<String, Object> context = buildContext(propDoc);
        VelocityEngine engine = new VelocityEngine();

        try (Reader reader = createTemplateReader(); Writer writer = new OutputStreamWriter(out)) {
            engine.evaluate(new VelocityContext(context), writer,
                    String.format("[%s]", getClass().getName()), reader);
        }
    }

    /**
     * A class-path resource path that identifies the velocity template to use.
     * @return
     */
    public String getTemplateFile() {
        return templateFile;
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
        // java -Dpropdoc.output.path=C:\Projects\IBM\ekopol\prop-doc\.env -Dproperties.file.url=file:C:\Projects\IBM\ekopol\prop-doc\application.properties -Dvelocity.template.url=classpath:ca/mrvisser/propdoc/velocity/template.env.vm -jar .\target\prop-doc-1.0-SNAPSHOT-jar-with-dependencies.jar
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

    private Reader createTemplateReader() throws MalformedURLException, IOException {
        InputStream is = ResourceUtil.createInputStreamFromUrl(getTemplateFile());
        return new InputStreamReader(is);
    }
}
