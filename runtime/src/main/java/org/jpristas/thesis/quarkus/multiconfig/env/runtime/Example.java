package org.jpristas.thesis.quarkus.multiconfig.env.runtime;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Example {

    public String example() {
        return "This text is from extension";
    }
}
