package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface TaskController {
    @Operation(summary = "Get a list of all tasks")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "List of tasks",
            content = @Content(schema = @Schema(implementation = Task.class))
    )
    ResponseEntity<Iterable<Task>> getAllTasks(@QuerydslPredicate(root = Task.class) Predicate predicate);

    @Operation(summary = "Get a task by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<Task> getTask(@PathVariable Long id);

    @Operation(summary = "Create a new task")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    ResponseEntity<Task> createTask(@RequestBody @Valid TaskDto taskDto);

    @Operation(summary = "Update the task")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDto taskDto);

    @Operation(summary = "Delete the task")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    ResponseEntity<Void> deleteTask(@PathVariable Long id);
}
