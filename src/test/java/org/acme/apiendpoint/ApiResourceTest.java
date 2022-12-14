package org.acme.apiendpoint;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ApiResourceTest {

  private static final String MOCK_PODNAME = "my-podname";

    @BeforeAll
    public static void beforeAll() {
      System.setProperty("POD_NAME", MOCK_PODNAME);
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/ping")
          .then()
             .statusCode(200)
             .body(is("PONG"));
    }

    @Test
    public void testStatus() {
        given()
          .when().get("/status/503")
          .then()
             .statusCode(503);
    }

    @Test
    public void testStatusWhenInvalidNumber() {
        given()
          .when().get("/status/005")
          .then()
            .statusCode(400);
    }

    @Test
    public void testStatusWhenNoNumberButString() {
        given()
          .when().get("/status/foo")
          .then()
            .statusCode(400);
    }

    @Test
    public void testPodnameIsReturned() {
      given()
        .when().get("/podname")
        .then()
        .statusCode(200)
        .body(is(MOCK_PODNAME));
    }
}