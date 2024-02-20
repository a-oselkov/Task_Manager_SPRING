package hexlet.code.controller.impl;

import hexlet.code.controller.TaskStatusController;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

import static hexlet.code.controller.impl.TaskStatusControllerImpl.TASKSTATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASKSTATUS_CONTROLLER_PATH)
public class TaskStatusControllerImpl implements TaskStatusController {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;
    public static final String TASKSTATUS_CONTROLLER_PATH = "/statuses";
    public static final String TASKSTATUS_ID = "/{id}";

    @GetMapping
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @GetMapping(TASKSTATUS_ID)
    public TaskStatus getTaskStatus(@PathVariable final Long id) {
        return taskStatusService.getTaskStatus(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus createTaskStatus(@RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping(TASKSTATUS_ID)
    @PreAuthorize("hasAuthority('USER')")
    public TaskStatus updateTaskStatus(@PathVariable final Long id,
                                       @RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping(TASKSTATUS_ID)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteTaskStatus(@PathVariable final Long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
