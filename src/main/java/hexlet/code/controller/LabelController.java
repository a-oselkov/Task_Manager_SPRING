package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LabelController {
    @Operation(summary = "Get a list of all labels")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "List of tasks",
            content = @Content(schema = @Schema(implementation = Label.class))
    )
    List<Label> getLabels();

    @Operation(summary = "Get a label by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    Label getLabel(@PathVariable Long id);

    @Operation(summary = "Create a new label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label created",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    Label createLabel(@RequestBody @Valid LabelDto labelDto);

    @Operation(summary = "Update the label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    Label updateUser(@PathVariable Long id,
                     @RequestBody @Valid LabelDto labelDto);

    @Operation(summary = "Delete the label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    void deleteUser(@PathVariable Long id);
}
