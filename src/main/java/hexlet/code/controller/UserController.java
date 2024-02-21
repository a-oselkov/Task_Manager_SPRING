package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserController {
    @Operation(summary = "Get a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    ResponseEntity<List<User>> getUsers();

    @Operation(summary = "Get a user by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<User> getUser(@Parameter(description = "Id of user to be searched")
                 @PathVariable Long id);

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    ResponseEntity<User> createUser(@RequestBody @Valid UserDto userDto);

    @Operation(summary = "Update the user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    ResponseEntity<User> updateUser(@Parameter(description = "Id of user to be updated")
                    @PathVariable Long id,
                    @RequestBody @Valid UserDto userDto);

    @Operation(summary = "Delete the user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    ResponseEntity<Void> deleteUser(@Parameter(description = "Id of user to be deleted")
                    @PathVariable Long id);
}
