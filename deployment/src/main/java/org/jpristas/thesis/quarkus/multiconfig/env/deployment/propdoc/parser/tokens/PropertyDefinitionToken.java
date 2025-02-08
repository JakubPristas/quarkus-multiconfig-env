package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.tokens;

public class PropertyDefinitionToken extends BaseToken {
    private String key;

    public PropertyDefinitionToken(String text, String key) {
        super(text);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", getText(), getKey());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PropertyDefinitionToken other = (PropertyDefinitionToken) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        if (!super.equals(obj))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }
}
