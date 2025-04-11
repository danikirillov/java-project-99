package hexlet.code.app.exception;

public class LabelNotFoundException extends RuntimeException {
    public LabelNotFoundException(Long id) {
        super("Label with id " + id + " not found");
    }
} 