package io.github.joshaby.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public record PostResponse(String text, LocalDateTime createdAt) {
}
