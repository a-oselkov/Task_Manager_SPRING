package hexlet.code.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(@NotBlank String firstName,
                      @NotBlank String lastName,
                      @Email @NotBlank String email,
                      @NotBlank @Size(min = 3) String password) {

}
