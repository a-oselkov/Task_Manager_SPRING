package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabelDto {
    @NotBlank
    private String name;
}
