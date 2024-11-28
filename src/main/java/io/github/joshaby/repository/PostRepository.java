package io.github.joshaby.repository;

import io.github.joshaby.domain.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import javax.naming.Context;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
