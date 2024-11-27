package io.github.joshaby.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Age is required") Integer age) {
}
