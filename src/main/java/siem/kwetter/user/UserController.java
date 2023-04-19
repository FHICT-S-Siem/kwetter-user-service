package siem.kwetter.user;

import lombok.AllArgsConstructor;

import javax.inject.Inject;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Path("/api/v1/userstate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService userService;

    @GET
    public Response getProfiles() {
        List<User> users = User.listAll();
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
    @Transactional
    public Response create(User user) {
        User.persist(user);
        if (user.isPersistent()){
            return Response.created(URI.create("/userstate" + user.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long id, User user) {
        try {
            return Response.ok(userService.replace(id, user)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                return Response.status(Status.NOT_FOUND).entity(Map.of("userState", e.getMessage())).build();
            }

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
