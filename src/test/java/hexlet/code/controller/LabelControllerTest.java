package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigTests;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
import static hexlet.code.controller.LabelController.ID;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.TEST_LABEL;
import static hexlet.code.utils.TestUtils.TEST_LABEL_UPD;
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
class LabelControllerTest {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void regUser() throws Exception {
        utils.regByNotAuthorizedUser(utils.getTestRegistrationDto(), USER_CONTROLLER_PATH);
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createLabelNotAuthorized() throws Exception {
        assertThat(labelRepository.count()).isEqualTo(0);
        utils.regByNotAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH)
                .andExpect(status().isForbidden());
        assertThat(labelRepository.count()).isEqualTo(0);
    }

    @Test
    public void createLabelAuthorized() throws Exception {
        assertThat(labelRepository.count()).isEqualTo(0);

        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH)
                .andExpect(status().isCreated());

        final Label label = labelRepository.findAll().get(0);

        assertThat(labelRepository.count()).isEqualTo(1);
        assertThat(label.getName()).isEqualTo(TEST_LABEL);
    }

    @Test
    void getLabels() throws Exception {
        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH);
        final MockHttpServletResponse response = utils.perform(get(LABEL_CONTROLLER_PATH),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<Label> labels = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(labels).hasSize(1);
    }

    @Test
    void getLabelById() throws Exception {
        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH);
        final Label expectedLabel = labelRepository.findAll().get(0);
        final MockHttpServletResponse response = utils.perform(
                        get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Label label = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertThat(label.getId()).isEqualTo(expectedLabel.getId());
        assertThat(label.getName()).isEqualTo(expectedLabel.getName());
    }

    @Test
    public void updateLabel() throws Exception {
        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH);
        final Label oldLabel = labelRepository.findAll().get(0);
        final LabelDto updLabelDto = new LabelDto(TEST_LABEL_UPD);
        final Long labelId = oldLabel.getId();

        final MockHttpServletRequestBuilder updateRequest = put(
                LABEL_CONTROLLER_PATH + ID, labelId)
                .content(TestUtils.asJson(updLabelDto))
                .contentType(APPLICATION_JSON);
        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        final Label updLabel = labelRepository.findAll().get(0);

        assertThat(labelRepository.existsById(labelId)).isTrue();
        assertThat(updLabel.getId()).isEqualTo(oldLabel.getId());
        assertThat(updLabel.getName()).isEqualTo(TEST_LABEL_UPD);
    }

    @Test
    public void deleteLabel() throws Exception {
        utils.regByAuthorizedUser(utils.getTestLabelDto(), LABEL_CONTROLLER_PATH);
        final Label label = labelRepository.findAll().get(0);
        utils.perform(delete(LABEL_CONTROLLER_PATH + ID, label.getId()),
                        TEST_USERNAME)
                .andExpect(status().isOk());
        assertThat(labelRepository.count()).isEqualTo(0);
    }
}
