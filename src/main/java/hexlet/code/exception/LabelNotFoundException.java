package hexlet.code.exception;

import jakarta.persistence.EntityNotFoundException;

public class LabelNotFoundException extends EntityNotFoundException {
    public LabelNotFoundException(Long id) {
        super("Label with id " + id + " not found");
    }
} 