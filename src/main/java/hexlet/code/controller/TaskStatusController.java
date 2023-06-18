package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import static hexlet.code.controller.TaskStatusController.TASKSTATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("${base-url}" + TASKSTATUS_CONTROLLER_PATH)
public class TaskStatusController {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;
    public static final String TASKSTATUS_CONTROLLER_PATH = "/statuses";
    public static final String TASKSTATUS_ID = "/{id}";

    @Operation(summary = "Get a list of all task statuses")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "List of task statuses",
            content = @Content(schema = @Schema(implementation = TaskStatus.class))
    )
    @GetMapping
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Operation(summary = "Get a task status by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task status",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    @GetMapping(TASKSTATUS_ID)
    public TaskStatus getTaskStatus(@PathVariable final Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Status not found"));
    }

    @Operation(summary = "Create a new task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status created",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus createTaskStatus(@RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @Operation(summary = "Update the task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated",
                    content = @Content(schema = @Schema(implementation = TaskStatus.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PutMapping(TASKSTATUS_ID)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus updateTaskStatus(@PathVariable final Long id,
                                       @RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @Operation(summary = "Delete the task status")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found")
    })
    @DeleteMapping(TASKSTATUS_ID)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteTaskStatus(@PathVariable final Long id) {
        taskStatusRepository.deleteById(id);
    }
}
