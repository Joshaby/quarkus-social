package io.github.joshaby.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record UserRequest(
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Age is required") Integer age) {
}
