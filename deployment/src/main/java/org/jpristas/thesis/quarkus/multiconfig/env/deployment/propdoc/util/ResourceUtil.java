package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ResourceUtil {
    public final static String CLASSPATH_PROTOCOL = "classpath";
    public final static String FILE_PROTOCOL = "file";
    public final static String CLASSPATH_PROTOCOL_PREFIX = String.format("%s:", CLASSPATH_PROTOCOL);
    public final static String FILE_PROTOCOL_PREFIX = String.format("%s:", FILE_PROTOCOL);

    public final static InputStream createInputStreamFromUrl(String url) throws MalformedURLException, IOException {
        if (!url.contains(":"))
            url = String.format("%s%s", FILE_PROTOCOL_PREFIX, url);

        if (url.startsWith(CLASSPATH_PROTOCOL_PREFIX)) {
            url = url.substring(CLASSPATH_PROTOCOL_PREFIX.length());
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
        } else {
            return (new URL(url)).openStream();
        }
    }
}
