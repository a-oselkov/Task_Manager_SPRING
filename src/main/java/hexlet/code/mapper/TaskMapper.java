package hexlet.code.mapper;

import hexlet.code.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface TaskMapper {

    void updateTask(@MappingTarget Task target, Task source);
}
