package io.github.joshaby.controller;

import io.github.joshaby.domain.Post;
import io.github.joshaby.dto.PostRequest;
import io.github.joshaby.dto.PostResponse;
import io.github.joshaby.repository.PostRepository;
import io.github.joshaby.repository.UserRepository;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

@Path("/users/{id}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PostController {

    private final PostRepository repository;

    private final UserRepository userRepository;

    @POST
    @Transactional
    public Response create(@PathParam("id") Long id, PostRequest request, @Context UriInfo uriInfo) {
        return userRepository.findByIdOptional(id).map(user -> {
            Post post = new Post();
            post.setText(request.text());
            post.setUser(user);
            repository.persist(post);

            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path("/users/{id}/posts/{postId}")
                    .build(user.getId(), post.getId());
            return Response.created(uri).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response listAll(@PathParam("id") Long id) {
        return userRepository.findByIdOptional(id).map(
                        user -> {
                            List<PostResponse> posts =
                                    repository.find("user", Sort.by("createdAt", Sort.Direction.Descending), user)
                                            .list().stream().map(post -> new PostResponse(post.getText(), post.getCreatedAt())).toList();
                            return Response.ok(posts).build();
                        })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
