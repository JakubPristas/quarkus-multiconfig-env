package org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.parser.tokens;

public class CommentToken extends BaseToken {
    private String content;

    public CommentToken(String text, String content) {
        super(text);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", getText(), getContent());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommentToken other = (CommentToken) obj;
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (!super.equals(obj))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 67 * hash + (this.content != null ? this.content.hashCode() : 0);
        return hash;
    }
}
