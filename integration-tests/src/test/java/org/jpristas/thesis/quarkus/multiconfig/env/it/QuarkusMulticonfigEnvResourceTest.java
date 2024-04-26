package org.jpristas.thesis.quarkus.multiconfig.env.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusMulticonfigEnvResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-multiconfig-env")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-multiconfig-env"));
    }
}
