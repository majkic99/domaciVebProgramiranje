package rafnews.backend.resources;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import rafnews.backend.model.Comment;
import rafnews.backend.services.CommentService;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;

    @GET
    @Path("/{perPage}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> all(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage)
    {
        return this.commentService.allComments(page,  perPage);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Comment create(@Valid Comment komentar) {
        return this.commentService.addComment(komentar);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment find(@PathParam("id") Integer id) {
        return this.commentService.findComment(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Integer id) {
        this.commentService.deleteComment(id);
    }
    
    @GET
	@Path("/like/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void likeComment(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		this.commentService.likeComment(id, request.getSession(true).getId());
	}
	
	@GET
	@Path("/dislike/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dislikeComment(@PathParam("id") Integer id,@Context HttpServletRequest request) {
		this.commentService.dislikeComment(id, request.getSession(true).getId());
	}
	
	@GET
	@Path("/karma/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer karma(@PathParam("id") Integer id) {
		return this.commentService.karma(id);
	}
	
	@GET
	@Path("/reactions/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer reactions(@PathParam("id") Integer id) {
		return this.commentService.reactions(id);
	}

}