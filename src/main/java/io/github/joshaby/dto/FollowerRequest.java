package io.github.joshaby.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FollowerRequest(Long followerId) {
}
