package org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.parser.tokens;

public class CommentAttributeToken extends CommentToken {
    private String name;

    public CommentAttributeToken(String text, String name, String content) {
        super(text, content);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", getText(), getName(), getContent());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommentAttributeToken other = (CommentAttributeToken) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (!super.equals(obj))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
