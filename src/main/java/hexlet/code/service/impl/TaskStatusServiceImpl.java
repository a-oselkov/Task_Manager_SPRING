package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createTaskStatus(final TaskStatusDto taskStatusDto) {
        final TaskStatus taskStatus = fromDto(taskStatusDto);
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(final Long id, final TaskStatusDto taskStatusDto) {
        final TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task status with id = " + id + " not found"));
        merge(taskStatus, taskStatusDto);
        return taskStatus;
    }

    private TaskStatus fromDto(TaskStatusDto taskStatusDto) {
        return TaskStatus.builder()
                .name(taskStatusDto.getName())
                .build();
    }
    private void merge(final TaskStatus taskStatus, final TaskStatusDto taskStatusDto) {
        final TaskStatus newTaskStatus = fromDto(taskStatusDto);
        taskStatus.setName(newTaskStatus.getName());
    }
}
