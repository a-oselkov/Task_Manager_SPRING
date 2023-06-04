package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createTaskStatus(final TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(final Long id, final TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).get();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }
}
