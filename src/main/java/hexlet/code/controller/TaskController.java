package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import java.util.NoSuchElementException;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String TASK_ID = "/{id}";
    private static final String TASK_OWNER =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @Operation(summary = "Get a list of all tasks")
    @ApiResponse(responseCode = "200", description = "List of tasks",
            content = @Content(schema = @Schema(implementation = Task.class))
    )
    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) final Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Operation(summary = "Get a task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping(TASK_ID)
    public Task getTask(@PathVariable final Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
    }

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Task createTask(@RequestBody @Valid final TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @Operation(summary = "Update the task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PutMapping(TASK_ID)
    @PreAuthorize("hasAuthority('USER')")
    public Task updateTask(@PathVariable final Long id, @RequestBody @Valid final TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @Operation(summary = "Delete the task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @DeleteMapping(TASK_ID)
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final Long id) {
        taskRepository.deleteById(id);
    }
}
