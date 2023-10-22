package com.anma.rest.contr;

import com.anma.models.db.Page;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/rest/api/page")
@ApplicationScoped
public class PageResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Page>> getPages(@RestQuery long limit) {
        return Page.list("id < ?1", Sort.by("id", Sort.Direction.Descending), limit);
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Page>> allPages() {
        return Page.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Page> getPageById(@PathParam("id") long id) {
        return Page.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createPage(Page page) {
        return Panache.<Page>withTransaction(page::persist)
                .onItem().transform(inserted -> Response.created(URI.create("/rest/api/page/" + inserted.id)).build());
    }
}
