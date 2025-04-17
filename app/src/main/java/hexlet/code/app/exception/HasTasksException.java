package hexlet.code.app.exception;

public class HasTasksException extends RuntimeException {
    public HasTasksException(Long id) {
        super("Cannot delete entity with id " + id + " because it is used in tasks");
    }
} 