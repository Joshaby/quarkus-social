package io.github.joshaby.dto;

import java.time.LocalDateTime;

public record PostResponse(String text, LocalDateTime createdAt) {
}
