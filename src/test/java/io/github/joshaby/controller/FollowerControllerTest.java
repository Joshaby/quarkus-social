package io.github.joshaby.controller;

import io.github.joshaby.domain.Follower;
import io.github.joshaby.domain.User;
import io.github.joshaby.dto.FollowerRequest;
import io.github.joshaby.repository.FollowerRepository;
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

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerController.class)
class FollowerControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;

    Long followerId;

    @BeforeEach
    @Transactional
    public void setUp() {
        User user = new User();
        user.setName("José");
        user.setAge(23);

        userRepository.persist(user);
        userId = user.getId();

        User follower = new User();
        follower.setName("José");
        follower.setAge(23);

        userRepository.persist(follower);
        followerId = follower.getId();

        Follower followerEntity = new Follower(user, follower);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("Should return 409 when follower id is equals to user id")
    public void sameUserAsFollowerTest() {

        FollowerRequest request = new FollowerRequest(userId);

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .pathParam("userId", userId)
                .when()
                    .put()
                .then()
                    .extract().response();

        String message = response.jsonPath().getString("message");
        assertEquals(409, response.getStatusCode());
        assertEquals("You can't follow yourself", message);
    }

    @Test
    @DisplayName("Should return 404 on follow a user when user id doesn't exist")
    public void userNotFoundWhenTryingToFollowTest() {

        FollowerRequest request = new FollowerRequest(userId);

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("userId", 999)
        .when()
            .put()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should follow user")
    public void followUserTest() {

        FollowerRequest request = new FollowerRequest(followerId);

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("userId", userId)
        .when()
            .put()
        .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("Should return 404 on list user followers and user id doesn't exist")
    public void userNotFoundWhenListingFollowersTest() {

        given()
            .contentType(ContentType.JSON)
            .pathParam("userId", 999)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should list a user's followers")
    public void listFollowersTest() {

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .extract().response();

        Integer followersCount = response.jsonPath().get("followersCount");
        List<Object> followers = response.jsonPath().getList("followers");

        assertEquals(200, response.getStatusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followers.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow user and user id doesn't exist")
    public void userNotFoundWhenUnfollowingAUserTest() {

        given()
            .pathParam("userId", 999)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Should unfollow an user")
    public void unfollowUserTest() {

        given()
            .pathParam("userId", userId)
            .queryParam("followerId", followerId)
        .when()
            .delete()
        .then()
            .statusCode(204);
    }
}