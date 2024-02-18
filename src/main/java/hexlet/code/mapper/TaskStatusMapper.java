package hexlet.code.mapper;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface TaskStatusMapper {

    TaskStatus toTaskStatus(TaskStatusDto dto);
    void updateTaskStatus(@MappingTarget TaskStatus taskStatus, TaskStatusDto dto);
}
