package io.github.joshaby.controllers;

import io.github.joshaby.dto.UserRequest;
import io.github.joshaby.model.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @POST
    @Transactional
    public Response create(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setName(request.name());
        user.setAge(request.age());
        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(User.listAll()).build();
    }
}
