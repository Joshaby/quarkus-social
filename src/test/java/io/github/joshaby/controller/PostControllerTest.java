package io.github.joshaby.controller;

import io.github.joshaby.domain.Follower;
import io.github.joshaby.domain.Post;
import io.github.joshaby.domain.User;
import io.github.joshaby.dto.PostRequest;
import io.github.joshaby.repository.FollowerRepository;
import io.github.joshaby.repository.PostRepository;
import io.github.joshaby.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
class PostControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;

    Long userNotFollowerId;

    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp() {

        User user = new User();
        user.setName("José");
        user.setAge(23);
        userRepository.persist(user);
        userId = user.getId();

        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);

        User userNotFollower = new User();
        userNotFollower.setName("José");
        userNotFollower.setAge(23);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        User userFollower = new User();
        userFollower.setName("José");
        userFollower.setAge(23);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower(user, userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should create a post for a user")
    public void create() {

        PostRequest request = new PostRequest("Bom dia grupo");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("userId", userId)
        .when()
            .post()
        .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("Should return 4040 when trying to make a post for an inexisten user")
    public void postForAnInexistentUser() {
        PostRequest request = new PostRequest("Bom dia grupo");

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("userId", 999)
        .when()
            .post()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should return 4040 when user doesn't exist")
    public void listUserNotFoundTest() {

        given()
            .pathParam("userId", 999)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listFollowerHeaderNotSendTest() {

        Response response =
                given()
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .extract().response();

        String responseMessage = response.jsonPath().getString("message");

        assertEquals(400, response.statusCode());
        assertEquals("Inexistent followerId", responseMessage);
    }

    @Test
    @DisplayName("Should return 404 when follower doesn't exist")
    public void listFollowerNotFoundTest() {

        given()
            .pathParam("userId", userId)
            .header("followerId", 999)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should return 403 when follower isn1t a follower")
    public void listNotAFollowerTest() {

        Response response =given()
            .pathParam("userId", userId)
            .header("followerId", userNotFollowerId)
        .when()
            .get()
        .then()
            .extract().response();

        String responseMessage = response.jsonPath().getString("message");

        assertEquals(403, response.statusCode());
        assertEquals("You can't see these posts", responseMessage);
    }

    @Test
    @DisplayName("Should list posts")
    public void listTest() {

        given()
            .pathParam("userId", userId)
            .header("followerId", userFollowerId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}