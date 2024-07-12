package org.jpristas.thesis.quarkus.multiconfig.env.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/quarkus-multiconfig-env/example-code-generator")
@ApplicationScoped
public class ExampleCodeGeneratorResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getGeneratedFileContent() {
        java.nio.file.Path filePath = Paths.get("target", "generated-data.txt");

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }
}
