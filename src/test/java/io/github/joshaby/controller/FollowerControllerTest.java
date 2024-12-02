package io.github.joshaby.controller;

import io.github.joshaby.domain.User;
import io.github.joshaby.dto.PostRequest;
import io.github.joshaby.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
class FollowerControllerTest {

    @Inject
    UserRepository userRepository;
    Long userId;

    @BeforeEach
    @Transactional
    public void setUp() {
        User user = new User();
        user.setName("José");
        user.setAge(23);

        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("Should create a post for a user")
    public void create() {

        PostRequest request = new PostRequest("Bom dia meus amigos");

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .pathParam("userId", userId)
                .when()
                    .post()
                .then()
                    .extract().response();

        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for an inexistent user")
    public void createForAnInexistentUser() {

        PostRequest request = new PostRequest("Bom dia meus amigos");

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .pathParam("userId", 999)
                .when()
                    .post()
                .then()
                    .extract().response();

        assertEquals(404, response.statusCode());
    }
}