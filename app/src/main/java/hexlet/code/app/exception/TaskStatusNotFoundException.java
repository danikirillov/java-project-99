package hexlet.code.app.exception;

public class TaskStatusNotFoundException extends RuntimeException {
    public TaskStatusNotFoundException(Long id) {
        super("TaskStatus with id " + id + " not found");
    }

    public TaskStatusNotFoundException(String status) {
        super("Task status not found: " + status);
    }
} 