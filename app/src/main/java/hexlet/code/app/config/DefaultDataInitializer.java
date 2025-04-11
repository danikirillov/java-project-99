package hexlet.code.app.config;

import hexlet.code.app.dto.TaskStatusCreateRequest;
import hexlet.code.app.dto.UserCreateRequest;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initAdminUser();
        initDefaultTaskStatuses();
    }

    private void initAdminUser() {
        var userData = new UserCreateRequest();
        userData.setFirstName("Admin");
        userData.setLastName("Adminov");
        userData.setEmail("admin@ad.min");
        userData.setPassword("hello");
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

    private TaskStatusCreateRequest createTaskStatusRequest(String name, String slug) {
        var request = new TaskStatusCreateRequest();
        request.setName(name);
        request.setSlug(slug);
        return request;
    }
}

