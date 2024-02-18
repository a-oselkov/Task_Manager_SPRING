package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
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

public interface TaskStatusController {
    @Operation(summary = "Get a list of all task statuses")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "List of task statuses",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))
    )
    List<TaskStatus> getTaskStatuses();

    @Operation(summary = "Get a task status by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task status",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    TaskStatus getTaskStatus(@PathVariable Long id);

    @Operation(summary = "Create a new task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status created",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    TaskStatus createTaskStatus(@RequestBody @Valid TaskStatusDto taskStatusDto);

    @Operation(summary = "Update the task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    TaskStatus updateTaskStatus(@PathVariable Long id,
                                @RequestBody @Valid TaskStatusDto taskStatusDto);

    @Operation(summary = "Delete the task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found")
    })
    void deleteTaskStatus(@PathVariable Long id);
}
