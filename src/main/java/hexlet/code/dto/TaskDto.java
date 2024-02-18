package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TaskDto(@NotBlank String name,
                      String description,
                      @NotNull Long taskStatusId,
                      Long executorId,
                      List<Long> labelIds) {

}
