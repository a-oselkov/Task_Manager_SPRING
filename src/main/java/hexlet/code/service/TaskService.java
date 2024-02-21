package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

public interface TaskService {

    Task createTask(TaskDto taskDto);
    Task updateTask(Long id, TaskDto taskDto);
    Task getTask(Long id);
    Iterable<Task> getAllTasks(Predicate predicate);
    void deleteTask(Long id);
}
