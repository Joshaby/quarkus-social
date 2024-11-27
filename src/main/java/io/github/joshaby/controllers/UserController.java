package io.github.joshaby.controllers;

import io.github.joshaby.dto.UserRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @POST
    public Response create(UserRequest request) {
        return Response.ok(request).build();
    }
}
