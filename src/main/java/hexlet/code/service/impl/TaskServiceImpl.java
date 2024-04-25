package hexlet.code.service.impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.exception.TaskNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper mapper;

    @Override
    public Task createTask(final TaskDto taskDto) {
        final Task task = fromDto(taskDto);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(final Long id, final TaskDto taskDto) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id = " + id + " not found"));
        mapper.updateEntity(task, fromDto(taskDto));
        return task;
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    public Iterable<Task> getAllTasks(Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }

    private Task fromDto(TaskDto taskDto) {
        final User author = userService.getCurrentUser();
        final TaskStatus taskStatus = taskStatusRepository.findById(taskDto.taskStatusId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Task Status with id " + taskDto.taskStatusId() + " for Task not found")
                );
        final User executor = userRepository.findById(taskDto.executorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Executor with id " + taskDto.executorId() + " for Task not found")
                );
        final List<Label> labels = labelRepository.findAllById(taskDto.labelIds());

        return Task.builder()
                .name(taskDto.name())
                .description(taskDto.description())
                .taskStatus(taskStatus)
                .author(author)
                .executor(executor)
                .labels(labels)
                .build();
    }
}
