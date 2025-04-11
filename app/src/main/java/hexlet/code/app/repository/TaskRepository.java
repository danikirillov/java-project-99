package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByAssignee(User user);

    boolean existsByTaskStatus(TaskStatus taskStatus);

    boolean existsByLabelsContains(Label label);
} 