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
import rafnews.backend.model.Keyword;
import rafnews.backend.model.News;
import rafnews.backend.services.NewsService;

@Path("/news")
public class NewsResource {

	@Inject
	private NewsService newsService;

	@GET
	@Path("/{perPage}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<News> all(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage) {
		return this.newsService.allNews(page, perPage);
	}

	@GET
	@Path("/visits/{perPage}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<News> allNewsByVisits(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage) {
		return this.newsService.allNewsByVisits(page, perPage);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public News create(@Valid News vesti) {
		return this.newsService.addNews(vesti);
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public News updateNews(@Valid News vesti) {
		return this.newsService.updateNews(vesti);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public News find(@PathParam("id") Integer id) {
		return this.newsService.findNews(id);
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("id") Integer id) {
		this.newsService.deleteNews(id);
	}

	@GET
	@Path("/kategorija/{name}/{perPage}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<News> allByCategory(@PathParam("name") String name, @PathParam("page") Integer page,
			@PathParam("perPage") Integer perPage) {
		return this.newsService.allByCategory(name, page, perPage);
	}

	@GET
	@Path("/tag/{id}/{perPage}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<News> allByTag(@PathParam("id") Integer id, @PathParam("page") Integer page,
			@PathParam("perPage") Integer perPage) {
		return this.newsService.allByTag(id, page, perPage);
	}

	@GET
	@Path("/newsTag/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Keyword> allTagByNews(@PathParam("id") Integer id) {
		return this.newsService.allTagByNews(id);
	}

	@GET
	@Path("/comments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> allCommentsByNews(@PathParam("id") Integer id) {
		return this.newsService.allCommentsByNews(id);
	}
	
	@GET
	@Path("/like/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void likeNews(@PathParam("id") Integer id,@Context HttpServletRequest request) {
		this.newsService.likeNews(id, request.getSession(true).getId());
	}
	
	@GET
	@Path("/dislike/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void dislikeNews(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		this.newsService.dislikeNews(id,request.getSession(true).getId());
	}
	
	@GET
	@Path("/karma/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer karma(@PathParam("id") Integer id) {
		return this.newsService.karma(id);
	}
	
	@GET
	@Path("/reactions/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer reactions(@PathParam("id") Integer id) {
		return this.newsService.reactions(id);
	}
	
	
	
}