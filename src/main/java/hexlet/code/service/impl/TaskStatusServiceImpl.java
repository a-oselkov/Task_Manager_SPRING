package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.TaskStatusException;
import hexlet.code.mapper.TaskStatusMapper;
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
    private final TaskStatusMapper mapper;

    @Override
    public TaskStatus createTaskStatus(final TaskStatusDto taskStatusDto) {
        final TaskStatus taskStatus = mapper.toTaskStatus(taskStatusDto);
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(final Long id, final TaskStatusDto taskStatusDto) {
        final TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task status with id = " + id + " not found"));
        mapper.updateTaskStatus(taskStatus, taskStatusDto);
        return taskStatus;
    }

    @Override
    public TaskStatus getTaskStatus(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new TaskStatusException("Task status not found"));
    }

    @Override
    public void deleteTaskStatus(Long id) {
        TaskStatus taskStatus = getTaskStatus(id);
        taskStatusRepository.delete(taskStatus);
    }
}
