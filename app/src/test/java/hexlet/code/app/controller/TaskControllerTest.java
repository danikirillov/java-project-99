package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskCreateRequest;
import hexlet.code.app.dto.TaskResponse;
import hexlet.code.app.dto.TaskUpdateRequest;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.util.TestModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskMapper taskMapper;

    private JwtRequestPostProcessor token;
    private Task testTask;
    private TaskStatus testStatus;
    private User testUser;
    private Label testLabel1;
    private Label testLabel2;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
            .apply(springSecurity())
            .build();

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testStatus);

        testLabel1 = Instancio.of(modelGenerator.getLabelModel()).create();
        testLabel2 = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel1);
        labelRepository.save(testLabel2);

        testTask = Instancio.of(modelGenerator.getTaskModel())
            .set(field(Task::getTaskStatus), testStatus)
            .set(field(Task::getAssignee), testUser)
            .set(field(Task::getLabels), Set.of(testLabel1))
            .create();
        taskRepository.save(testTask);

        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    @Transactional
    void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        var body = response.getContentAsString();

        List<TaskResponse> actual = om.readValue(body, new TypeReference<>() {});
        var expected = taskRepository.findAll().stream()
            .map(taskMapper::toResponse)
            .toList();

        assertThat(actual).hasSize(expected.size());
        
        // Find the test task in the response
        var testTaskResponse = actual.stream()
            .filter(task -> task.getId().equals(testTask.getId()))
            .findFirst()
            .orElseThrow();

        // Verify the test task properties
        assertThat(testTaskResponse.getTitle()).isEqualTo(testTask.getName());
        assertThat(testTaskResponse.getContent()).isEqualTo(testTask.getDescription());
        assertThat(testTaskResponse.getStatus()).isEqualTo(testTask.getTaskStatus().getName());
        assertThat(testTaskResponse.getAssigneeId()).isEqualTo(testTask.getAssignee().getId());
        assertThat(testTaskResponse.getTaskLabelIds()).contains(testLabel1.getId());
    }

    @Test
    @Transactional
    void testCreate() throws Exception {
        var task = Instancio.of(modelGenerator.getTaskModel())
            .set(field(Task::getTaskStatus), testStatus)
            .set(field(Task::getAssignee), testUser)
            .create();

        var request = new TaskCreateRequest();
        request.setTitle(task.getName());
        request.setContent(task.getDescription());
        request.setStatus(testStatus.getSlug());
        request.setAssigneeId(testUser.getId());
        request.setTaskLabelIds(Set.of(testLabel1.getId(), testLabel2.getId()));

        mockMvc.perform(post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

        var createdTask = taskRepository.findAll().stream()
            .filter(t -> t.getName().equals(task.getName()))
            .findFirst()
            .orElse(null);

        assertNotNull(createdTask);
        assertThat(createdTask.getName()).isEqualTo(task.getName());
        assertThat(createdTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(createdTask.getTaskStatus().getName()).isEqualTo(testStatus.getName());
        assertThat(createdTask.getAssignee().getId()).isEqualTo(testUser.getId());
        assertThat(createdTask.getLabels()).hasSize(2);
        assertThat(createdTask.getLabels()).contains(testLabel1, testLabel2);
    }

    @Test
    @Transactional
    void testUpdate() throws Exception {
        var data = new TaskUpdateRequest();
        data.setTitle(JsonNullable.of("New Title"));
        data.setContent(JsonNullable.of("New Content"));
        data.setTaskLabelIds(JsonNullable.of(Set.of(testLabel2.getId())));

        var request = put("/api/tasks/" + testTask.getId())
            .with(token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(task.getName()).isEqualTo("New Title");
        assertThat(task.getDescription()).isEqualTo("New Content");
        assertThat(task.getLabels()).hasSize(1);
        assertThat(task.getLabels()).contains(testLabel2);
    }

    @Test
    void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId()).with(jwt());
        var result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            v -> v.node("title").isEqualTo(testTask.getName()),
            v -> v.node("content").isEqualTo(testTask.getDescription()),
            v -> v.node("taskLabelIds").isArray().contains(testLabel1.getId()));
    }

    @Test
    void testDelete() throws Exception {
        var request = delete("/api/tasks/" + testTask.getId())
            .with(token);
        mockMvc.perform(request)
            .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }
} 