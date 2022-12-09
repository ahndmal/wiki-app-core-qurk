package com.anma.rest.contr;

import com.anma.models.db.Blog;
import com.anma.models.db.Page;
import com.anma.models.db.Space;
import com.anma.repo.CommentRepo;
import com.anma.rest.dto.StatsDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
public class StatsController {

    @Inject CommentRepo commentRepo;

    @Path("/rest/api/stats/")
    @GET
    public StatsDto getStats() {
        long pages = Page.count().await().indefinitely();
        long spaces = Space.count().await().indefinitely();
        long blogs = Blog.count().await().indefinitely();
        long comments = commentRepo.count().await().indefinitely();
        return new StatsDto(pages, spaces, blogs, comments);
    }
}
