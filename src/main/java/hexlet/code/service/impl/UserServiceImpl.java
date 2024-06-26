package hexlet.code.service.impl;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.UserNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public User createUser(final UserDto userDto) {
        final String password = passwordEncoder.encode(userDto.password());
        final User user = mapper.toEntity(userDto);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final Long id, final UserDto userDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        mapper.updateEntity(user, userDto);
        return user;
    }

    @Override
    public User getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'username': " + username));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = getCurrentUser();
        userRepository.delete(user);
    }
}
