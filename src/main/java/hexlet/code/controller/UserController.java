package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String USER_ID = "/{id}";
    private static final String ACCOUNT_OWNER = """
                @userRepository.findById(#id).get().getEmail() == authentication.getName()
            """;

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "Get a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get a user by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(USER_ID)
    public User getUser(@Parameter(description = "Id of user to be searched")
                        @PathVariable final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public User createUser(@RequestBody @Valid final UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Operation(summary = "Update the user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PutMapping(USER_ID)
    @PreAuthorize(ACCOUNT_OWNER)
    public User updateUser(@Parameter(description = "Id of user to be updated")
                           @PathVariable final Long id,
                           @RequestBody @Valid final UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(summary = "Delete the user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @DeleteMapping(USER_ID)
    @PreAuthorize(ACCOUNT_OWNER)
    public void deleteUser(@Parameter(description = "Id of user to be deleted")
                           @PathVariable final Long id) {
        userRepository.deleteById(id);
    }
}

