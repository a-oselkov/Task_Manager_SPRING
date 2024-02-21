package hexlet.code.controller.impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.controller.TaskController;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.impl.TaskControllerImpl.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Task>> getAllTasks(@QuerydslPredicate(root = Task.class) final Predicate predicate) {
        return ResponseEntity.status(OK).body(taskService.getAllTasks(predicate));
    }

    @GetMapping(value = TASK_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable final Long id) {
        return ResponseEntity.status(OK).body(taskService.getTask(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Task> createTask(@RequestBody @Valid final TaskDto taskDto) {
        return ResponseEntity.status(CREATED).body(taskService.createTask(taskDto));
    }

    @Override
    @PutMapping(value = TASK_ID, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Task> updateTask(@PathVariable final Long id, @RequestBody @Valid final TaskDto taskDto) {
        return ResponseEntity.status(OK).body(taskService.updateTask(id, taskDto));
    }

    @DeleteMapping(TASK_ID)
    @PreAuthorize(TASK_OWNER)
    public ResponseEntity<Void> deleteTask(@PathVariable final Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(OK).build();
    }
}
