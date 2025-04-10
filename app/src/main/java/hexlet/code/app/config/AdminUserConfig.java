package hexlet.code.app.config;

import hexlet.code.app.dto.UserCreateRequest;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserConfig implements ApplicationRunner {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateRequest();
        userData.setFirstName("Admin");
        userData.setLastName("Adminov");
        userData.setEmail("admin@ad.min");
        userData.setPassword("hello");
        var user = userMapper.toEntity(userData);
        userRepository.save(user);
        log.info("admin user was created");
    }
}

