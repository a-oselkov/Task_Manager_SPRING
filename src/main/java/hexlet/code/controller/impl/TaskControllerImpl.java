package hexlet.code.controller.impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.controller.TaskController;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

import static hexlet.code.controller.impl.TaskControllerImpl.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskControllerImpl implements TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String TASK_ID = "/{id}";
    private static final String TASK_OWNER =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) final Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @GetMapping(TASK_ID)
    public Task getTask(@PathVariable final Long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Task createTask(@RequestBody @Valid final TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @Override
    @Operation(summary = "Update the task")
    @SecurityRequirement(name = "Bearer Authentication")
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

    @DeleteMapping(TASK_ID)
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final Long id) {
        taskService.deleteTask(id);
    }
}
