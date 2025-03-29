package org.jpristas.thesis.quarkus.multiconfig.deployment.propdoc.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class JavaPropertyFileTokenizer {
    private JavaPropertyFileTokenizer() {}

    /**
     * Tokenize the given input stream into prop-doc tokens.
     *
     * @param in A java properties file input stream
     * @return A TokenEnumeration that may be used to iterate over prop-doc {@code Token}s
     * @throws IOException Thrown if there is an issue reading the input stream
     */
    public static TokenEnumeration tokenize(InputStream in) throws IOException {
        List<String> lines = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null)
                lines.add(line);
            return new TokenEnumeration(lines.toArray(new String[0]));
        }
    }
}
