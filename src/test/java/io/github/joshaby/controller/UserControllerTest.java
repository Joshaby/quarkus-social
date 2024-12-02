package io.github.joshaby.controller;

import io.github.joshaby.dto.UserRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestHTTPEndpoint(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Test
    @DisplayName("Should create an user successfully")
    @Order(1)
    public void createTest() {

        UserRequest request = new UserRequest("José", 23);

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post()
                .then()
                    .extract().response();

        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Should return error when JSON is not valid")
    @Order(2)
    public void createValidationErrorTest() {

        UserRequest request = new UserRequest(null, null);

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post()
                .then()
                    .extract().response();

        assertEquals(UserController.UNPROCESSABLE_ENTITY, response.statusCode());
        assertEquals("Validation errors", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.getFirst().get("message"));
        assertNotNull(errors.get(1).get("message"));
    }

    @Test
    @DisplayName("Should list all users")
    @Order(3)
    public void listAllTest() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}