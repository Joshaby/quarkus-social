package io.github.joshaby.controllers;

import io.github.joshaby.dto.UserRequest;
import io.github.joshaby.model.User;
import io.github.joshaby.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @POST
    @Transactional
    public Response create(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setAge(request.age());
        repository.persist(user);

        URI uri = UriBuilder.fromResource(UserController.class).path("/{id}").build(user.getId());
        return Response.created(uri).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(repository.listAll()).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequest request) {
        return repository.findByIdOptional(id).map(user -> {
            user.setName(request.name());
            user.setAge(request.age());

            return Response.noContent().build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return repository.findByIdOptional(id).map(user -> {
            repository.delete(user);
            return Response.noContent().build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
