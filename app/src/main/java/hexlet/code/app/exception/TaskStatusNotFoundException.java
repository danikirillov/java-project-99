package hexlet.code.app.exception;

public class TaskStatusNotFoundException extends RuntimeException {
    public TaskStatusNotFoundException(Long id) {
        super("TaskStatus with id " + id + " not found");
    }
} 