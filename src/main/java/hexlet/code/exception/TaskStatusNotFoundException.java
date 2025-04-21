package hexlet.code.exception;

import jakarta.persistence.EntityNotFoundException;

public class TaskStatusNotFoundException extends EntityNotFoundException {
    public TaskStatusNotFoundException(Long id) {
        super("TaskStatus with id " + id + " not found");
    }

    public TaskStatusNotFoundException(String status) {
        super("Task status not found: " + status);
    }
} 