package hexlet.code.app.util;

import hexlet.code.app.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TestModelGenerator {
    private Model<User> userModel;

    private final Faker faker = new Faker();

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
            .ignore(Select.field("id"))
            .supply(Select.field("firstName"), () -> faker.name().firstName())
            .supply(Select.field("lastName"), () -> faker.name().lastName())
            .supply(Select.field("email"), () -> faker.internet().emailAddress())
            .supply(Select.field("password"), () -> faker.internet().password())
            .ignore(Select.field("createdAt"))
            .ignore(Select.field("updatedAt"))
            .toModel();
    }
}
