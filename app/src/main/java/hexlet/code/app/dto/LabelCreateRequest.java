package hexlet.code.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelCreateRequest {
    @Size(min = 3, max = 1000)
    private String name;
} 