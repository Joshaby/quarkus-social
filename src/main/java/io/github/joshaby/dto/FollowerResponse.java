package io.github.joshaby.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record FollowerResponse(Integer followersCount, List<UserResponse> followers) {
}
