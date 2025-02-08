package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.tokens;

import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.parser.Token;

public class BaseToken implements Token {
    private String text;

    BaseToken(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("{%s}", getText());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseToken other = (BaseToken) obj;
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
}
