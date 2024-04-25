package hexlet.code.mapper;

import hexlet.code.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface TaskMapper extends Mappable<Task, Task> {
}
