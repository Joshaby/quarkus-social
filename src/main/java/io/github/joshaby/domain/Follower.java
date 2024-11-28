package io.github.joshaby.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "followers")
@Getter
@Setter
@NoArgsConstructor
public class Follower {

    @EmbeddedId
    private FollowerPK id;

    public Follower(User user, User follower) {
        this.id = new FollowerPK(user, follower);
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowerPK {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "follower_id")
        private User follower;
    }
}
