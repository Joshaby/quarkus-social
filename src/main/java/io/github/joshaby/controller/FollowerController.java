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

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerRepository followerRepository;

    private final UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request) {
        return userRepository.findByIdOptional(userId).map(
                        user -> userRepository.findByIdOptional(request.followerId()).map(userFollower -> {
                            Follower follower = new Follower(user, userFollower);
                            followerRepository.persist(follower);

                            return Response.noContent().build();
                        }).orElse(Response.status(Response.Status.NOT_FOUND).build()))
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
