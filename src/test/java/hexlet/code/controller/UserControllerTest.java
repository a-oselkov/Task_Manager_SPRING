package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigTests;
import hexlet.code.dto.LoginDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static hexlet.code.config.SpringConfigTests.TEST_PROFILE;
import static hexlet.code.config.security.SecurityConfig.LOGIN;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_ID;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.TEST_USERNAME_NEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigTests.class)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void registration() throws Exception {
        assertThat(userRepository.count()).isEqualTo(0);
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH)
                .andExpect(status().isCreated());
        assertThat(userRepository.count()).isEqualTo(1);

        final User user = userRepository.findByEmail(TEST_USERNAME).get();
        assertThat(user.getFirstName()).isEqualTo(utils.getTestRegistrationDto().getFirstName());
        assertThat(user.getLastName()).isEqualTo(utils.getTestRegistrationDto().getLastName());
        assertThat(user.getEmail()).isEqualTo(utils.getTestRegistrationDto().getEmail());
    }

    @Test
    public void getUserById() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
        final User expectedUser = userRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                get(USER_CONTROLLER_PATH + USER_ID, expectedUser.getId()),
                expectedUser.getEmail()
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(expectedUser.getId()).isEqualTo(user.getId());
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void getAllUsers() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
        final MockHttpServletResponse response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void twiceRegTheSameUserFail() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH)
                .andExpect(status().isCreated());
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH)
                .andExpect(status().is(422));
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void login() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
        final LoginDto loginDto = new LoginDto(
                utils.getTestRegistrationDto().getEmail(),
                utils.getTestRegistrationDto().getPassword()
        );
        final MockHttpServletRequestBuilder loginRequest = post(LOGIN)
                .content(TestUtils.asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    public void loginFail() throws Exception {
        final LoginDto loginDto = new LoginDto(
                utils.getTestRegistrationDto().getEmail(),
                utils.getTestRegistrationDto().getPassword()
        );
        final MockHttpServletRequestBuilder loginRequest = post(LOGIN)
                .content(TestUtils.asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);

        final User oldUser = userRepository.findByEmail(TEST_USERNAME).get();
        final Long userId = oldUser.getId();

        final UserDto userDto = new UserDto("newName", "newLastName",
                TEST_USERNAME_NEW, "newPwd");

        final MockHttpServletRequestBuilder updateRequest = put(USER_CONTROLLER_PATH + USER_ID, userId)
                .content(TestUtils.asJson(userDto))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        final User newUser = userRepository.findByEmail(TEST_USERNAME_NEW).get();
        assertThat(userRepository.existsById(userId)).isTrue();
        assertThat(newUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(newUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(newUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    public void deleteUser() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID, userId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertThat(userRepository.count()).isEqualTo(0);
    }

    @Test
    public void deleteUserByNotOwner() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);

        final UserDto notOwnerUserDto = new UserDto(
                "fname",
                "lname",
                TEST_USERNAME_NEW,
                "pwd");

        utils.regByNotAuthorizedUser(notOwnerUserDto, USER_CONTROLLER_PATH);

        final Long ownerUserId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID, ownerUserId), TEST_USERNAME_NEW)
                .andExpect(status().isForbidden());

        assertThat(userRepository.count()).isEqualTo(2);
    }
}
