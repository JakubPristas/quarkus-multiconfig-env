package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.impl.writer;

import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDoc;
import org.apache.commons.lang.StringUtils;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.PropDocWriter;
import org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api.Property;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

public class PropertiesFilePropDocWriter implements PropDocWriter {
    private static final int DEFAULT_COMMENT_WIDTH = 100;
    private static final boolean DEFAULT_OUTPUT_PROPERTY_META_VALUE = false;
    private static final String FMT_COMMENT = "# %s\n";
    private static final String FMT_COMMENT_ATTRIBUTE = "# @%s %s\n";
    private static final String FMT_COMMENT_BLOCK = "##\n";
    private static final String FMT_PROPERTY_DEF = "%s=%s\n";
    private static final String FMT_EMPTY_LINE = "\n";
    private static final String FMT_WORD = " %s";
    private static final String CHARSET = "ISO-8859-1";
    private static final Comparator<String> ATTR_COMPARATOR = (one, other) -> {
        if (StringUtils.equals(one, other))
            return 0;
        if (Property.ATTR_DEFAULT_KEY.equals(one))
            return -1;
        if (Property.ATTR_DEFAULT_KEY.equals(other))
            return 1;
        if (Property.ATTR_PROPERTY_KEY.equals(one))
            return -1;
        if (Property.ATTR_PROPERTY_KEY.equals(other))
            return 1;
        if (one == null)
            return -1;
        if (other == null)
            return 1;
        return one.compareTo(other);
    };

    private int commentWidth = DEFAULT_COMMENT_WIDTH;

    private boolean outputPropertyMetaValue = DEFAULT_OUTPUT_PROPERTY_META_VALUE;

    public PropertiesFilePropDocWriter() {}

    public PropertiesFilePropDocWriter(int commentWidth) {
        this.commentWidth = commentWidth;
    }

    /* (non-Javadoc)
     * @see PropDocWriter#write(PropDoc, java.io.OutputStream)
     */
    @Override
    public void write(PropDoc propDoc, OutputStream out) throws IOException {
        for (Property property : propDoc) {
            writeEmptyLine(out);

            Set<String> attributeKeys = new LinkedHashSet<>();
            attributeKeys.addAll(property.getMetadataKeys());

            //first output all the meta-data for the property in a comment block above the property
            if (attributeKeys != null && !attributeKeys.isEmpty()) {

                //starts the comment block
                writeCommentBlock(out);

                //write the property key as the documentation property with a new comment line after
                if (outputPropertyMetaValue) {
                    writeCommentAttribute(out, Property.ATTR_PROPERTY_KEY, property.getKey());
                    writeComment(out);
                }

                //write all other "user-defined" documentation properties
                for (String name : attributeKeys) {
                    String val = property.getMetadataValue(name);
                    if (val != null && !val.isEmpty()) {
                        /*
                         * Basically, we're going to assume the comment content is a plain-text paragraph, which
                         * is bad form if there is HTML comment formatting.
                         */

                        //clean the string up. We don't need no stinkin' new lines either
                        val = val.replaceAll("\\s+", " ");
                        String[] words = val.split("\\s");
                        int w = 0, //index for the word array
                                l = 0; //what line number we're on

                        /*
                         * we split the text up into "words", we have to output like this to support sane
                         * wrapping to avoid overflowing the commentWidth.
                         */
                        while (w < words.length) {
                            StringBuilder currentLine = new StringBuilder(words[w]);
                            int lineSize = words[w].length();
                            w++;
                            l++;

                            //not to get technical, but there is some comment pre-amble on each line..
                            if (l == 1 && !name.equals(Property.ATTR_DEFAULT_KEY)) {
                                //include the actual meta-data key (e.g., @description) if this is the first line
                                lineSize += 3 + name.length();
                            } else {
                                lineSize += 2;
                            }

                            //find how much of the comment we can actually output on this comment line
                            while (w < words.length) {
                                lineSize += words[w].length() + 1;
                                if (lineSize > commentWidth)
                                    break;

                                currentLine.append(String.format(FMT_WORD, words[w]));
                                w++;
                            }

                            if (l == 1 && !name.equals(Property.ATTR_DEFAULT_KEY)) {
                                writeCommentAttribute(out, name, currentLine.toString());
                            } else {
                                writeComment(out, currentLine.toString());
                            }
                        }

                        if (name.equals(Property.ATTR_DEFAULT_KEY)) {
                            writeComment(out);
                        }
                    }
                }
                writeCommentBlock(out);
            }
            writePropertyDef(out, property.getKey(), convertPropertyValue(property.getValue()));
        }
    }

    private String convertPropertyValue(String val) {
        return val.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r")
                .replace("\f", "\\f");
    }

    private void writePropertyDef(OutputStream out, String name, String val) throws IOException {
        write(out, String.format(FMT_PROPERTY_DEF, name, val));
    }

    private void writeCommentAttribute(OutputStream out, String name, String content)
            throws IOException {
        write(out, String.format(FMT_COMMENT_ATTRIBUTE, name, content));
    }

    private void writeComment(OutputStream out) throws IOException {
        writeComment(out, "");
    }

    private void writeComment(OutputStream out, String content) throws IOException {
        write(out, String.format(FMT_COMMENT, content));
    }

    private void writeCommentBlock(OutputStream out) throws IOException {
        write(out, FMT_COMMENT_BLOCK);
    }

    private void writeEmptyLine(OutputStream out) throws IOException {
        write(out, FMT_EMPTY_LINE);
    }

    private void write(OutputStream out, String content) throws IOException {
        out.write(content.getBytes(CHARSET));
    }
}
