package org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.parser;

import org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.parser.tokens.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenFactory {
    private static final TokenFactory instance = new TokenFactory();

    public static TokenFactory getInstance() {
        return instance;
    }

    private static final Pattern COMMENT_BLOCK_PATTERN = Pattern.compile("^[\\s]*##(.*)$");

    private static final Pattern COMMENT_ATTRIBUTE_PATTERN =
            Pattern.compile("^[\\s]*#[\\s]*@([\\w-]*[\\w+]+)[\\s]*(.*)$");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^[\\s]*#(.*)$");

    private static final Pattern PROPERTY_DEFINITION_PATTERN =
            Pattern.compile("^[\\s]*([\\w\\.\"\\-\\[\\]\\?\\!'\\*\\&\\%\\/]+)=.*$");
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^[\\s]*$");

    public Token createToken(String line) {
        /*
         * comment block and comment attribute are by nature "comment" patterns as well. Best to check
         * for block and attributes first to avoid being short-circuited
         */
        Matcher m = COMMENT_BLOCK_PATTERN.matcher(line);
        if (m.matches())
            return createCommentBlockToken(m);

        m = COMMENT_ATTRIBUTE_PATTERN.matcher(line);
        if (m.matches())
            return createCommentAttributeToken(m);

        m = COMMENT_PATTERN.matcher(line);
        if (m.matches())
            return createCommentToken(m);

        m = PROPERTY_DEFINITION_PATTERN.matcher(line);
        if (m.matches())
            return createPropertyDefinitionToken(m);

        m = EMPTY_LINE_PATTERN.matcher(line);
        if (m.matches())
            return createEmptyLineToken(m);

        return null;
    }

    public Token createEndOfFileToken() { return new EndOfFileToken(""); }

    private CommentBlockToken createCommentBlockToken(Matcher m) { return new CommentBlockToken(m.group(0), m.group(1)); }

    private CommentAttributeToken createCommentAttributeToken(Matcher m) {
        return new CommentAttributeToken(m.group(0), m.group(1), m.group(2));
    }

    private CommentToken createCommentToken(Matcher m) {
        return new CommentToken(m.group(0), m.group(1));
    }

    private PropertyDefinitionToken createPropertyDefinitionToken(Matcher m) {
        return new PropertyDefinitionToken(m.group(0), m.group(1));
    }

    private EmptyLineToken createEmptyLineToken(Matcher m) {
        return new EmptyLineToken(m.group(0));
    }

}
