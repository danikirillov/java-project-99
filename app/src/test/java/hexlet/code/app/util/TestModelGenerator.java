package hexlet.code.app.util;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.model.Label;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;

import hexlet.code.app.model.Task;

@Getter
@Component
public class TestModelGenerator {
    private Model<User> userModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    private final Faker faker = new Faker();

    @PostConstruct
    public void init() {
        userModel = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
            .supply(Select.field(User::getLastName), () -> faker.name().lastName())
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .supply(Select.field(User::getPassword), () -> faker.internet().password())
            .ignore(Select.field(User::getCreatedAt))
            .ignore(Select.field(User::getUpdatedAt))
            .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
            .ignore(Select.field(TaskStatus::getId))
            .supply(Select.field(TaskStatus::getName), () -> faker.lorem().word())
            .supply(Select.field(TaskStatus::getSlug), () -> faker.lorem().word())
            .ignore(Select.field(TaskStatus::getCreatedAt))
            .ignore(Select.field(TaskStatus::getUpdatedAt))
            .toModel();

        taskModel = Instancio.of(Task.class)
            .ignore(Select.field(Task::getId))
            .ignore(Select.field(Task::getCreatedAt))
            .supply(Select.field(Task::getName), () -> "Test Task")
            .supply(Select.field(Task::getDescription), () -> "Test Description")
            .toModel();

        labelModel = Instancio.of(Label.class)
            .ignore(Select.field(Label::getId))
            .supply(Select.field(Label::getName), () -> faker.lorem().word())
            .ignore(Select.field(Label::getCreatedAt))
            .toModel();
    }
}
