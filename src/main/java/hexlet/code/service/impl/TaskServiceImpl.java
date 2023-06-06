package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public Task createTask(@RequestBody final TaskDto taskDto) {
        Task task = new Task();
        User author = userService.getCurrentUser();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setAuthor(author);
        task.setTaskStatus(taskStatus);
        task.setExecutor(executor);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).get();
        User author = task.getAuthor();
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();
        User executor = userRepository.findById(taskDto.getExecutorId()).get();

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setAuthor(author);
        task.setTaskStatus(taskStatus);
        task.setExecutor(executor);
        return taskRepository.save(task);
    }
}
