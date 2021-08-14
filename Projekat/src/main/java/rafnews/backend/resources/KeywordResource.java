package rafnews.backend.resources;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rafnews.backend.model.Keyword;
import rafnews.backend.services.KeywordService;

@Path("/keywords")
public class KeywordResource {
	@Inject
    private KeywordService keywordService;

    @GET
    @Path("/{perPage}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Keyword> all(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage)
    {
        return this.keywordService.allKeywords(page,  perPage);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Keyword create(@Valid Keyword Keyword) {
        return this.keywordService.addKeyword(Keyword);
    }

    @GET
    @Path("/{main_word}")
    @Produces(MediaType.APPLICATION_JSON)
    public Keyword find(@PathParam("main_word") String id) {
        return this.keywordService.findKeyword(id);
    }

    @GET
    @Path("by/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Keyword findTagById(@PathParam("id") Integer id) {
        return this.keywordService.findKeywordById(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Integer id) {
        this.keywordService.deleteKeyword(id);
    }
}
