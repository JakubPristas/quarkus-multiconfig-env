package org.jpristas.thesis.quarkus.multiconfig.env.deployment.propdoc.api;

import java.io.IOException;
import java.io.OutputStream;

public interface PropDocWriter {

    /**
     * Write the given PropDoc model to the output stream.
     * @param propDoc
     * @param out
     */
    public void write(PropDoc propDoc, OutputStream out) throws IOException;
}
