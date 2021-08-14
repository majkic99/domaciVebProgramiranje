package rafnews.backend.resources;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rafnews.backend.model.User;
import rafnews.backend.requests.LoginRequest;
import rafnews.backend.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {

	@Inject
	private UserService userService;
	
	@GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }
	
	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response login(@Valid LoginRequest loginRequest) {
		Map<String, String> response = new HashMap<String, String>();

		String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
		if (jwt == null) {
			response.put("message", "These credentials do not match our records");
			return Response.status(422, "Unprocessable Entity").entity(response).build();
		}

		response.put("jwt", jwt);

		return Response.ok(response).build();
	}

	@GET
    @Path("/{perPage}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> all(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage) {
		return this.userService.allUser(page,  perPage);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public User create(@Valid User user) {
		return this.userService.addUser(user);
	}

	@GET
	@Path("/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public User find(@PathParam("email") String email) {
		return this.userService.findUser(email);
	}

	@GET
	@Path("/status/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer userActivity(@PathParam("email") String email) {
		return this.userService.userActivity(email);
	}

	@POST
	@Path("/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public User updateUser(@Valid User user, @PathParam("email") String email) {
		return this.userService.updateUser(user, email);
	}
}