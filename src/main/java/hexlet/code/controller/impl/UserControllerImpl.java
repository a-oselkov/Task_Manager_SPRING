package hexlet.code.controller.impl;

import hexlet.code.controller.UserController;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hexlet.code.controller.impl.UserControllerImpl.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserControllerImpl implements UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String USER_ID = "/{id}";
    private static final String ACCOUNT_OWNER = """
                @userRepository.findById(#id).get().getEmail() == authentication.getName()
            """;

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(OK).body(userService.getAllUsers());
    }

    @GetMapping(value = USER_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable final Long id) {
        return ResponseEntity.status(OK).body(userService.getUser(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody @Valid final UserDto userDto) {
        return ResponseEntity.status(CREATED).body(userService.createUser(userDto));
    }

    @PutMapping(value = USER_ID, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(ACCOUNT_OWNER)
    public ResponseEntity<User> updateUser(@PathVariable final Long id,
                           @RequestBody @Valid final UserDto userDto) {
        return ResponseEntity.status(OK).body(userService.updateUser(id, userDto));
    }

    @DeleteMapping(USER_ID)
    @PreAuthorize(ACCOUNT_OWNER)
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(OK).build();
    }
}

