package rafnews.backend.filters;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import rafnews.backend.resources.NewsResource;
import rafnews.backend.services.UserService;

import java.io.IOException;
import java.util.List;

//@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    UserService userService;

    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!this.isAuthRequired(requestContext)) {
            return;
        }

        String token = requestContext.getHeaderString("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        if (!this.userService.isAuthorized(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isAuthRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("login")) {
            return false;
        }

        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof NewsResource) {
                return true;
            }
        }

        return false;
    }
}
