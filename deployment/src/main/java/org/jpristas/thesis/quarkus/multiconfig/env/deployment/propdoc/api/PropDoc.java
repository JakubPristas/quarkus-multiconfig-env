package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api;

//import org.apache.commons.io.IOUtils;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.JavaPropertyFileTokenizer;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.Token;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.TokenEnumeration;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.tokens.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PropDoc implements Iterable<Property> {
    public static String ATTR_KEY = "key";

    private Map<String, Property> properties = new LinkedHashMap<>();

    public PropDoc() {
        //empty constructor for manually building a prop-doc model
    }

    public PropDoc(InputStream in) throws IOException {
//        byte[] fileContent = IOUtils.toByteArray(in);
        byte[] fileContent = in.readAllBytes();
        //first seed all the java properties and values using the java properties parser
        seedProperties(new ByteArrayInputStream(fileContent));

        //collect the meta-data documentation for each property
        parseMetadata(new ByteArrayInputStream(fileContent));
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    private void parseMetadata(InputStream in) throws IOException {
        TokenEnumeration tokens = JavaPropertyFileTokenizer.tokenize(in);
        //poor man's state machine..
        while (tokens.hasMoreElements()) {
            Token token = tokens.nextElement();

            if (token instanceof CommentBlockToken) {
                String propertyName = null;
                Map<String, String> attributeMap = new LinkedHashMap<>();
                String attributeName = Property.ATTR_DEFAULT_KEY;
                StringBuilder attributeContent = new StringBuilder(" ");
                StringBuilder defaultContent = new StringBuilder(" ");
                boolean blockDone = false;
                boolean isKeyAttributSet = false;

                while (tokens.hasMoreElements()) {
                    token = tokens.nextElement();

                    if (token instanceof CommentBlockToken) {
                        if (!blockDone) {
                            blockDone = true;
                            if (isKeyAttributSet) {
                                // at the end of the block, when @key is set, ignore the PropertyDefinitionToken if it exists
                                break;
                            }
                        } else {
                            //we've already finished this comment block and hit another block. exit with what we have
                            break;
                        }
                    } else if (token instanceof CommentAttributeToken) {
                        if (!blockDone) {
                            CommentAttributeToken t = (CommentAttributeToken) token;

                            //the default attribute content doesn't get added until the end, since it could still consume more content.
                            if (!Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
                                attributeMap.put(attributeName, attributeContent.toString().trim());
                            }

                            //set the new attribute name
                            attributeName = t.getName();

                            if (ATTR_KEY.equals(attributeName)) {
                                isKeyAttributSet = true;
                            }

                            //if the new attribute is the default attribute, append the content to the default buffer
                            if (Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
                                defaultContent.append(" ");
                                defaultContent.append(t.getContent());
                                attributeContent = new StringBuilder(" ");
                            } else {
                                attributeContent = new StringBuilder(" ");
                                attributeContent.append(t.getContent());
                            }

                            //if the new attribute is the "property" attribute, consume it immediately and revert to the default buffer
                            if (Property.ATTR_PROPERTY_KEY.equals(attributeName)) {
                                propertyName = attributeContent.toString().trim();
                                attributeName = Property.ATTR_DEFAULT_KEY;
                                attributeContent = new StringBuilder(" ");
                            }
                        }
                    } else if (token instanceof CommentToken) {
                        if (!blockDone) {
                            CommentToken t = (CommentToken) token;
                            if (Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
                                defaultContent.append(" ");
                                defaultContent.append(t.getContent());
                            } else {
                                attributeContent.append(" ");
                                attributeContent.append(t.getContent());
                            }
                        }
                    } else if (token instanceof PropertyDefinitionToken) {
                        if (propertyName == null) {
                            propertyName = ((PropertyDefinitionToken) token).getKey();
                        }
                        break;
                    } else if (token instanceof EndOfFileToken) {
                        break;
                    }
                }

                //finally apply the default buffer, as well as the last attribute content
                attributeMap.put(Property.ATTR_DEFAULT_KEY, defaultContent.toString().trim());
                if (!Property.ATTR_DEFAULT_KEY.equals(attributeName)) {
                    attributeMap.put(attributeName, attributeContent.toString().trim());
                }

                // set the propertyName with the value from the @key attribute
                if (isKeyAttributSet) {
                    propertyName = attributeMap.get(ATTR_KEY);
                }

                //apply this comment block to the property metadata
                if (propertyName != null) {
                    String value = null;
                    if (properties.containsKey(propertyName)) {
                        value = properties.get(propertyName).getValue();
                    } else {
                        //in this situation, there was a documented property in the property file that doesn't
                        //actually have a property-value declaration. we'll just store the property with a null
                        //value.
                    }

                    properties.put(propertyName, new Property(propertyName, value, attributeMap));
                }
            }
        }
    }

    @Override
    public Iterator<Property> iterator() {
        return properties.values().iterator();
    }

//    /**
//     * Use the Java properties parser to parse the properties file and seed all properties / values
//     * without the meta-data.
//     *
//     * @param file The java properties file
//     * @throws IOException Thrown if there is an issue reading the properties file.
//     */
    private void seedProperties(InputStream in) throws IOException {
        // Properties allProperties = new Properties();
        // try {
        //   allProperties.load(in);
        // } finally {
        //   in.close();
        // }
        List<Map.Entry<String, String>> propertyStrings =
                new String(in.readAllBytes(), StandardCharsets.UTF_8).lines()
                        .filter(line -> !line.strip().startsWith("#")).map(line -> {
                            String[] parts = line.split("=", 2);
                            if (parts.length != 2) {
                                //Log.warn("Line {} was incorrectly split to {}. Ignored", line, parts);
                                return null;
                            }
                            return Map.entry(parts[0], parts[1]);
                        }).filter(Objects::nonNull).toList();

        for (Map.Entry<String, String> entry : propertyStrings) {
            String key = entry.getKey();
            String value = entry.getValue();
            Property property = new Property(key, value, null);
            properties.put(key, property);
            // Log.info("Seed key: {}", property.getKey());
        }
    }

    public static void print(PropDoc propDoc) {
        //Log.info("Printing propdoc");
        for (Property property : propDoc) {
            //Log.info("Property key: {}", property.getKey());
        }
    }
}
