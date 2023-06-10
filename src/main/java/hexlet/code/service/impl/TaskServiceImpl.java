package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    @Override
    public Task createTask(final TaskDto taskDto) {
        Task task = fromDto(taskDto);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(final Long id, final TaskDto taskDto) {
        Task task = fromDto(taskDto);
        task.setId(id);
        return taskRepository.save(task);
    }

    private Task fromDto(TaskDto taskDto) {
        final User author = userService.getCurrentUser();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();
        List<Label> labels = taskDto.getLabelIds() == null ? null
                : labelRepository.findAllById(taskDto.getLabelIds());

        return Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .taskStatus(taskStatus)
                .author(author)
                .executor(executor)
                .labels(labels)
                .build();
    }
}
