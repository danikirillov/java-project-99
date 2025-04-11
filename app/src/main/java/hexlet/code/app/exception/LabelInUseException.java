package hexlet.code.app.exception;

public class LabelInUseException extends RuntimeException {
    public LabelInUseException(Long labelId) {
        super("Cannot delete label with id " + labelId + " because it is used in tasks");
    }
} 