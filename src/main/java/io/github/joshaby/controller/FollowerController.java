package io.github.joshaby.controller;

import io.github.joshaby.domain.Follower;
import io.github.joshaby.dto.FollowerRequest;
import io.github.joshaby.repository.FollowerRepository;
import io.github.joshaby.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerRepository repository;

    private final UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request) {

        if (userId.equals(request.followerId())) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("message", "You can't follow yourself")).build();
        }

        return userRepository.findByIdOptional(userId).map(
                        user -> userRepository.findByIdOptional(request.followerId()).map(userFollower -> {
                            if (!repository.follows(user, userFollower)) {
                                Follower follower = new Follower(user, userFollower);
                                repository.persist(follower);
                            }
                            return Response.noContent().build();
                        }).orElse(Response.status(Response.Status.NOT_FOUND).build()))
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
