package hexlet.code.dto.status;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskStatusResponse {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    @EqualsAndHashCode.Include
    private String slug;
    private LocalDate createdAt;
} 