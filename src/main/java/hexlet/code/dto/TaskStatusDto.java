package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskStatusDto(@NotBlank String name) {
}

