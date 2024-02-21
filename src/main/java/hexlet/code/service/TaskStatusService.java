package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {

    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(Long id, TaskStatusDto taskStatusDto);
    TaskStatus getTaskStatus(Long id);
    List<TaskStatus> getAllTaskStatuses();
    void deleteTaskStatus(Long id);

}
