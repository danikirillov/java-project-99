package hexlet.code.app.exception;

public class TaskStatusHasTasksException extends RuntimeException {
    public TaskStatusHasTasksException(Long taskStatusId) {
        super("Cannot delete task status with id " + taskStatusId + " because it has associated tasks");
    }
}