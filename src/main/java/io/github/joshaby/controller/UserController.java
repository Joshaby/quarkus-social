package io.github.joshaby.controller;

import io.github.joshaby.domain.User;
import io.github.joshaby.dto.UserRequest;
import io.github.joshaby.errors.dto.ResponseError;
import io.github.joshaby.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserController {

    private final static Integer UNPROCESSABLE_ENTITY = 422;

    private final UserRepository repository;

    private final Validator validator;

    @POST
    @Transactional
    public Response create(UserRequest request) {
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(
                    ResponseError.createFromValidation("Validation errors", violations)).build();
        }

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
