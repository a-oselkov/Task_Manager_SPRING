package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);
    User updateUser(Long id, UserDto userDto);
    User getCurrentUser();
    User getUser(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
}
