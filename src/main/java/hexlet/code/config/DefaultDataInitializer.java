package hexlet.code.config;

import hexlet.code.dto.status.TaskStatusCreateRequest;
import hexlet.code.dto.user.UserCreateRequest;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultDataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            initAdminUser();
            initDefaultTaskStatuses();
            initDefaultLabels();
        } catch (Exception ex) {
            Sentry.captureException(ex);
            log.error("init data error", ex);
        }
    }

    private void initAdminUser() {
        var userData = new UserCreateRequest();
        userData.setFirstName("Admin");
        userData.setLastName("Adminov");
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        var user = userMapper.toEntity(userData);
        userRepository.save(user);
        log.info("admin user was created");
    }

    private void initDefaultTaskStatuses() {
        List<TaskStatusCreateRequest> defaultStatuses = List.of(
            createTaskStatusRequest("Draft", "draft"),
            createTaskStatusRequest("To Review", "to_review"),
            createTaskStatusRequest("To Be Fixed", "to_be_fixed"),
            createTaskStatusRequest("To Publish", "to_publish"),
            createTaskStatusRequest("Published", "published")
        );

        defaultStatuses.forEach(statusRequest -> {
            if (taskStatusRepository.findBySlug(statusRequest.getSlug()).isEmpty()) {
                var status = taskStatusMapper.toEntity(statusRequest);
                taskStatusRepository.save(status);
            }
        });
        log.info("default task statuses were initialized");
    }

    private void initDefaultLabels() {
        List<String> defaultLabels = List.of("feature", "bug");

        defaultLabels.forEach(labelName -> {
            if (labelRepository.findByName(labelName).isEmpty()) {
                var label = new Label();
                label.setName(labelName);
                labelRepository.save(label);
            }
        });
        log.info("default labels were initialized");
    }

    private TaskStatusCreateRequest createTaskStatusRequest(String name, String slug) {
        var request = new TaskStatusCreateRequest();
        request.setName(name);
        request.setSlug(slug);
        return request;
    }
}

