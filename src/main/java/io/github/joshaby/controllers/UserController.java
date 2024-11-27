package io.github.joshaby.controllers;

import io.github.joshaby.dto.UserRequest;
import io.github.joshaby.model.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @POST
    @Transactional
    public Response create(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setAge(request.age());
        user.persist();

        URI uri = UriBuilder.fromResource(UserController.class).path("/{id}").build(user.getId());
        return Response.created(uri).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(User.listAll()).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UserRequest request) {
        return User.findByIdOptional(id).map(user -> {
            ((User) user).setName(request.name());
            ((User) user).setAge(request.age());

            return Response.noContent().build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return User.findByIdOptional(id).map(user -> {
            user.delete();
            return Response.noContent().build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
