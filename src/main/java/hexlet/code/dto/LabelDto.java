package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelDto(@NotBlank String name) {
}
