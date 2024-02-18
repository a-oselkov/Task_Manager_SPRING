package hexlet.code.mapper;


import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface UserMapper {

    User toUser(UserDto dto);
    void updateUser(@MappingTarget User user, UserDto dto);
}
