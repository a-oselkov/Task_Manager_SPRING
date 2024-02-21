package hexlet.code.controller.impl;

import hexlet.code.controller.TaskStatusController;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

import java.util.List;

import static hexlet.code.controller.impl.TaskStatusControllerImpl.TASKSTATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASKSTATUS_CONTROLLER_PATH)
public class TaskStatusControllerImpl implements TaskStatusController {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusService taskStatusService;
    public static final String TASKSTATUS_CONTROLLER_PATH = "/statuses";
    public static final String TASKSTATUS_ID = "/{id}";

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskStatus>> getTaskStatuses() {
        return ResponseEntity.status(OK).body(taskStatusService.getAllTaskStatuses());
    }

    @GetMapping(value = TASKSTATUS_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskStatus> getTaskStatus(@PathVariable final Long id) {
        return ResponseEntity.status(OK).body(taskStatusService.getTaskStatus(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TaskStatus> createTaskStatus(@RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return ResponseEntity.status(CREATED).body(taskStatusService.createTaskStatus(taskStatusDto));
    }

    @PutMapping(value = TASKSTATUS_ID, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable final Long id,
                                       @RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return ResponseEntity.status(OK).body(taskStatusService.updateTaskStatus(id, taskStatusDto));
    }

    @DeleteMapping(TASKSTATUS_ID)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable final Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.status(OK).build();
    }
}
