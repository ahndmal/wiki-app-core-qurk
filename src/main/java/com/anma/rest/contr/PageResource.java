package com.anma.rest.contr;

import com.anma.models.db.Page;
import com.anma.rest.req.CreatePageReq;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

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
    public Uni<List<Page>> getPages() {
        return Page.listAll(Sort.by("id").descending());
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
