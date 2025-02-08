package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Property implements Comparable<Property> {

    public static String

    ATTR_PROPERTY_KEY = "property",
    ATTR_DEFAULT_KEY = "description";

    private String key;
    private String value;
    private Map<String, String> metadata = new LinkedHashMap<>();

    public Property(String key, String value, Map<String, String> metadata) {
        this.key = key;
        this.value = value;

        if (metadata == null) {
            this.metadata = new LinkedHashMap<>();
        } else {
            this.metadata = metadata;
        }
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    Map<String, String> getMetadata() {
        return metadata;
    }

    public Set<String> getMetadataKeys() {
        return new LinkedHashSet<>(metadata.keySet());
    }

    public String getMetadataValue(String key) {
        return metadata.get(key);
    }


    @Override
    public int compareTo(Property o) {
        if (o == null)
            return 1;
        if (o.getKey() == null && getKey() == null)
            return 0;
        if (o.getKey() == null && getKey() != null)
            return 1;
        if (o.getKey() != null && getKey() == null)
            return -1;
        if (equals(o))
            return 0;
        if (getKey().equals(ATTR_DEFAULT_KEY))
            return -1;
        if (o.getKey().equals(ATTR_DEFAULT_KEY))
            return 1;
        return getKey().compareTo(o.getKey());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Property other = (Property) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }
}
