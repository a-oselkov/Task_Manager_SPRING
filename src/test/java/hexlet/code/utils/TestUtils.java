package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private JWTHelper jwtHelper;

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_NEW = "email_new@email.com";
    public static final String TEST_TASKSTATUS = "Done";
    public static final String TEST_TASKSTATUS_UPD = "In progress";
    public static final String TEST_LABEL = "java";
    public static final String TEST_LABEL_UPD = "python";

    private final UserDto testUserDto = new UserDto(
            "fname",
            "lname",
            TEST_USERNAME,
            "pwd"
    );

    private final TaskStatusDto testTaskStatusDto = new TaskStatusDto(
            TEST_TASKSTATUS
    );

    private final LabelDto testLabelDto = new LabelDto(
            TEST_LABEL
    );

    private final TaskDto testTaskDto = new TaskDto(
            "taskName",
            "description",
            1L,
            1L,
            List.of(1L)
    );

    public TaskDto getTestTaskDto() {
        return testTaskDto;
    }

    public UserDto getTestRegistrationDto() {
        return testUserDto;
    }

    public TaskStatusDto getTestTaskStatusDto() {
        return testTaskStatusDto;
    }

    public LabelDto getTestLabelDto() {
        return testLabelDto;
    }

    public void tearDown() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    public ResultActions regByAuthorizedUser(final Object dto,
                                             final String requestURI) throws Exception {
        final MockHttpServletRequestBuilder request = post(requestURI)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);
        return perform(request, TEST_USERNAME);
    }
    public ResultActions regByNotAuthorizedUser(final Object dto, final String requestURI) throws Exception {
        final MockHttpServletRequestBuilder request = post(requestURI)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
