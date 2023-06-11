package siem.kwetter.user;

import lombok.AllArgsConstructor;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Path("/api/v1/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService userService;

    @GET
    public Response getUsers() {
        List<User> users = userService.getUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        return User.findByIdOptional(id)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(User user) {
        Boolean isCreated = userService.create(user);
        if (isCreated){
            return Response.created(URI.create("/user" + user.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long id, User user) {
        try {
            Optional<User> foundUser = User.findByIdOptional(id);
            if (foundUser.isPresent()) {
                User r = foundUser.get();
                r.setUserState(user.userState);
                User updatedUser = userService.update(r);
                return Response.ok(updatedUser).build();
            }
            return Response.status(Status.NOT_FOUND).build();

        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") long id) {
        boolean deleted = User.deleteById(id);
        if(deleted){
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
