package hexlet.code.app.exception;

public class UserHasTasksException extends RuntimeException {
    public UserHasTasksException(Long userId) {
        super("Cannot delete user with id " + userId + " because they have associated tasks");
    }
} 