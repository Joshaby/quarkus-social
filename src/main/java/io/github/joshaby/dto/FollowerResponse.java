package io.github.joshaby.dto;

import java.util.List;

public record FollowerResponse(Integer followersCount, List<UserResponse> followers) {
}
