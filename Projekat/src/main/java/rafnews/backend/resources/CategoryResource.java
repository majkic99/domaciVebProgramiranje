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

import rafnews.backend.model.Category;
import rafnews.backend.services.CategoryService;

@Path("/category")
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Path("/{perPage}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> all(@PathParam("page") Integer page, @PathParam("perPage") Integer perPage)
    {
        return this.categoryService.allCategory( page,  perPage);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Category create(@Valid Category category) {
        return this.categoryService.addCategory(category);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category find(@PathParam("name") String name) {
        return this.categoryService.findCategory(name);
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("name") String name) {
        this.categoryService.deleteCategory(name);
    }

    @POST
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category update(@Valid Category category, @PathParam("name") String name)
    {
        return this.categoryService.updateCategory(category, name);
    }

}
