package io.github.joshaby.repository;

import io.github.joshaby.domain.Follower;
import io.github.joshaby.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User user, User follower) {
        PanacheQuery<Follower> query = find("id", new Follower.FollowerPK(user, follower));

        return !query.list().isEmpty();
    }

    public List<Follower> findByUser(User user) {
        return find("id.user", user).list();
    }
}
