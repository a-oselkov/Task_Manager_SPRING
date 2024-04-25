package hexlet.code.mapper;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface TaskStatusMapper extends Mappable<TaskStatus, TaskStatusDto> {
}
