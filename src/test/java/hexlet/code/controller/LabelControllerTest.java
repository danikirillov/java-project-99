package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateRequest;
import hexlet.code.dto.label.LabelResponse;
import hexlet.code.dto.label.LabelUpdateRequest;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.testutil.TestModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LabelControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelMapper labelMapper;

    private JwtRequestPostProcessor token;
    private Label testLabel;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
            .apply(springSecurity())
            .build();

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);

        token = jwt().jwt(builder -> builder.subject("admin@ad.min"));
    }

    @AfterEach
    void clean() {
        labelRepository.deleteAll();
    }

    @Test
    void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/labels").with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        var body = response.getContentAsString();

        List<LabelResponse> actual = om.readValue(body, new TypeReference<>() {});
        var expected = labelRepository.findAll().stream()
            .map(labelMapper::toResponse)
            .toList();

        assertThat(actual).hasSize(expected.size());
        
        var testLabelResponse = actual.stream()
            .filter(label -> label.getId().equals(testLabel.getId()))
            .findFirst()
            .orElseThrow();

        assertThat(testLabelResponse.getName()).isEqualTo(testLabel.getName());
        assertThat(testLabelResponse.getCreatedAt()).isNotNull();
    }

    @Test
    void testCreate() throws Exception {
        var label = Instancio.of(modelGenerator.getLabelModel()).create();

        var request = new LabelCreateRequest();
        request.setName(label.getName());

        mockMvc.perform(post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();

        var createdLabel = labelRepository.findAll().stream()
            .filter(l -> l.getName().equals(label.getName()))
            .findFirst()
            .orElse(null);

        assertThat(createdLabel).isNotNull();
        assertThat(createdLabel.getName()).isEqualTo(label.getName());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new LabelUpdateRequest();
        data.setName(JsonNullable.of("New Label Name"));

        var request = put("/api/labels/" + testLabel.getId())
            .with(token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));

        mockMvc.perform(request)
            .andExpect(status().isOk());

        var label = labelRepository.findById(testLabel.getId()).orElseThrow();
        assertThat(label.getName()).isEqualTo("New Label Name");
    }

    @Test
    void testShow() throws Exception {
        var request = get("/api/labels/" + testLabel.getId()).with(jwt());
        var result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            v -> v.node("name").isEqualTo(testLabel.getName()));
    }

    @Test
    void testDelete() throws Exception {
        var request = delete("/api/labels/" + testLabel.getId())
            .with(token);
        mockMvc.perform(request)
            .andExpect(status().isNoContent());

        assertThat(labelRepository.existsById(testLabel.getId())).isFalse();
    }
} 