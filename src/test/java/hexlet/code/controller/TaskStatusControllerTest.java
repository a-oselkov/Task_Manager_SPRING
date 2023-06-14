package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigTests;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static hexlet.code.config.SpringConfigTests.TEST_PROFILE;
import static hexlet.code.controller.TaskStatusController.TASKSTATUS_ID;
import static hexlet.code.controller.TaskStatusController.TASKSTATUS_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.TEST_TASKSTATUS;
import static hexlet.code.utils.TestUtils.TEST_TASKSTATUS_UPD;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigTests.class)
class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void clear() {
        utils.tearDown();
    }
    @BeforeEach
    public void regUser() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
    }

    @Test
    public void createTaskNotAuthorized() throws Exception {
        assertThat(taskStatusRepository.count()).isEqualTo(0);
        utils.regByNotAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH)
                .andExpect(status().isForbidden());
        assertThat(taskStatusRepository.count()).isEqualTo(0);
    }

    @Test
    public void createTaskAuthorized() throws Exception {
        assertThat(taskStatusRepository.count()).isEqualTo(0);

        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH)
                .andExpect(status().isCreated());
        final TaskStatus taskStatus = taskStatusRepository.findAll().get(0);

        assertThat(taskStatusRepository.count()).isEqualTo(1);
        assertThat(taskStatus.getName()).isEqualTo(TEST_TASKSTATUS);
    }

    @Test
    void getTaskStatusById() throws Exception {
        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH);
        final TaskStatus expectedTaskStatus = taskStatusRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                        get(TASKSTATUS_CONTROLLER_PATH + TASKSTATUS_ID, expectedTaskStatus.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        TaskStatus taskStatus = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(taskStatus.getId()).isEqualTo(expectedTaskStatus.getId());
        assertThat(taskStatus.getName()).isEqualTo(expectedTaskStatus.getName());
    }

    @Test
    void getTaskStatuses() throws Exception {
        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH);
        final MockHttpServletResponse response = utils.perform(get(TASKSTATUS_CONTROLLER_PATH),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<TaskStatus> tasks = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(tasks).hasSize(1);
    }

    @Test
    public void updateTaskStatus() throws Exception {
        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH);
        final TaskStatus oldTaskStatus = taskStatusRepository.findAll().get(0);
        final TaskStatusDto newTaskStatusDto = new TaskStatusDto(TEST_TASKSTATUS_UPD);

        final MockHttpServletRequestBuilder updateRequest = put(
                TASKSTATUS_CONTROLLER_PATH + TASKSTATUS_ID, oldTaskStatus.getId())
                .content(TestUtils.asJson(newTaskStatusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        final TaskStatus newTaskStatus = taskStatusRepository.findAll().get(0);

        assertThat(taskStatusRepository.existsById(oldTaskStatus.getId())).isTrue();
        assertThat(newTaskStatus.getId()).isEqualTo(oldTaskStatus.getId());
        assertThat(newTaskStatus.getName()).isEqualTo(TEST_TASKSTATUS_UPD);
    }

    @Test
    public void deleteTaskStatus() throws Exception {
        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH);
        final TaskStatus taskStatus = taskStatusRepository.findAll().get(0);
        utils.perform(delete(TASKSTATUS_CONTROLLER_PATH + TASKSTATUS_ID, taskStatus.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk());
        assertThat(taskStatusRepository.count()).isEqualTo(0);
    }
}
