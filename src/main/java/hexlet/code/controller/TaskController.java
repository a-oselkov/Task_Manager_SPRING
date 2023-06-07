package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private static final String ID = "/{id}";
    private static final String TASK_OWNER =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) final Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @GetMapping(ID)
    public Task getTask(@PathVariable final Long id) {
        return taskRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Task createTask(@RequestBody @Valid final TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping(ID)
    @PreAuthorize("hasAuthority('USER')")
    public Task updateTask(@PathVariable final Long id, @RequestBody @Valid final TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final Long id) {
        taskRepository.deleteById(id);
    }
}
