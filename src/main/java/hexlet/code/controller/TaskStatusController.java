package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
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

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskStatusService taskStatusService;

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    private static final String ID = "/{id}";

    @GetMapping
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @GetMapping(ID)
    public TaskStatus getTaskStatus(@PathVariable final Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Status not found"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus createTaskStatus(@RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping(ID)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus updateTaskStatus(@PathVariable final Long id,
                                       @RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize("hasAuthority('USER')")
    public String deleteTaskStatus(@PathVariable final Long id) {
        taskStatusRepository.deleteById(id);
        return "Status with id " + id + " deleted";
    }
}
