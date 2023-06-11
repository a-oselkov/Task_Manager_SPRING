package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigTests;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
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
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.TaskStatusController.TASKSTATUS_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
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
class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TestUtils utils;

    private TaskDto taskDto;

    @BeforeEach
    public void regComponent() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
        utils.regByAuthorizedUser(utils.getTestTaskStatusDto(), TASKSTATUS_CONTROLLER_PATH);
        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH);

        taskDto = new TaskDto(
                "task",
                "description",
                taskStatusRepository.findAll().get(0).getId(),
                userRepository.findAll().get(0).getId(),
                List.of(labelRepository.findAll().get(0).getId())
        );
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createTaskNotAuthorized() throws Exception {
        assertThat(taskRepository.count()).isEqualTo(0);
        utils.regByNotAuthorizedUser(taskDto, TASK_CONTROLLER_PATH)
                .andExpect(status().isForbidden());
        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    public void createTaskAuthorized() throws Exception {
        assertThat(taskRepository.count()).isEqualTo(0);

        utils.regByAuthorizedUser(taskDto, TASK_CONTROLLER_PATH)
                .andExpect(status().isCreated());

        final Task task = taskRepository.findAll().get(0);
        final User executor = userRepository.findById(taskDto.getExecutorId()).get();
        final List<Label> labels = labelRepository.findAll();

        assertThat(taskRepository.count()).isEqualTo(1);
        assertThat(task.getName()).isEqualTo(taskDto.getName());
        assertThat(task.getDescription()).isEqualTo(taskDto.getDescription());
        assertThat(task.getAuthor()).isEqualTo(userRepository.findByEmail(TEST_USERNAME).get());
        assertThat(task.getExecutor()).isEqualTo(executor);
        assertThat(task.getLabels().get(0)).isEqualTo(labels.get(0));
    }

    @Test
    void getTasks() throws Exception {
        utils.regByAuthorizedUser(taskDto, TASK_CONTROLLER_PATH);
        final MockHttpServletResponse response = utils.perform(get(TASK_CONTROLLER_PATH),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Task> tasks = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(tasks).hasSize(1);
    }

    @Test
    void getTaskById() throws Exception {
        utils.regByAuthorizedUser(taskDto, TASK_CONTROLLER_PATH);
        final Task expectedTask = taskRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                        get(TASK_CONTROLLER_PATH + ID, expectedTask.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Task task = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(task.getId()).isEqualTo(expectedTask.getId());
        assertThat(task.getName()).isEqualTo(expectedTask.getName());
    }

    @Test
    public void updateTask() throws Exception {
        utils.regByAuthorizedUser(taskDto, TASK_CONTROLLER_PATH);
        final Task oldTask = taskRepository.findAll().get(0);
        taskDto.setName("updName");
        taskDto.setDescription("updDescription");

        final MockHttpServletRequestBuilder updateRequest = put(
                TASK_CONTROLLER_PATH + ID, oldTask.getId())
                .content(TestUtils.asJson(taskDto))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        final Task updTask = taskRepository.findAll().get(0);
        final User executor = userRepository.findById(taskDto.getExecutorId()).get();
        final List<Label> labels = labelRepository.findAll();

        assertThat(taskRepository.existsById(oldTask.getId())).isTrue();
        assertThat(updTask.getId()).isEqualTo(oldTask.getId());
        assertThat(updTask.getName()).isEqualTo(taskDto.getName());
        assertThat(updTask.getDescription()).isEqualTo(taskDto.getDescription());
        assertThat(updTask.getAuthor()).isEqualTo(userRepository.findByEmail(TEST_USERNAME).get());
        assertThat(updTask.getExecutor()).isEqualTo(executor);
        assertThat(updTask.getLabels().get(0)).isEqualTo(labels.get(0));
    }

    @Test
    public void deleteTask() throws Exception {
        utils.regByAuthorizedUser(taskDto, TASK_CONTROLLER_PATH)
                .andExpect(status().isCreated());
        assertThat(taskRepository.count()).isEqualTo(1);
        final Task task = taskRepository.findAll().get(0);
        utils.perform(delete(TASK_CONTROLLER_PATH + ID, task.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk());
        assertThat(taskRepository.count()).isEqualTo(0);
    }
}
