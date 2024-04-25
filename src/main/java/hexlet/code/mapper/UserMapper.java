package hexlet.code.mapper;


import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface UserMapper extends Mappable<User, UserDto> {
}
