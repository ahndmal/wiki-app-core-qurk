package com.anma.repo;

import com.anma.models.db.Comment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentRepo implements PanacheRepository<Comment> {
}
